package com.example.gestion_rh.controller;

import com.example.gestion_rh.repository.PlaningEntretienRepository;
import com.example.gestion_rh.model.PlaningEntretien;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/calendrier")
public class CalendrierController {

    private final PlaningEntretienRepository repo;
    private static final Logger log = LoggerFactory.getLogger(CalendrierController.class);

    public CalendrierController(PlaningEntretienRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String show(@RequestParam(required = false) Integer year,
                       @RequestParam(required = false) Integer month,
                       Model model) {

        LocalDate now = LocalDate.now();
        int y = (year != null) ? year : now.getYear();
        int m = (month != null) ? month : now.getMonthValue();

        YearMonth ym = YearMonth.of(y, m);
        LocalDate firstOfMonth = ym.atDay(1);

        // start Monday
        LocalDate startCalendar = firstOfMonth.minusDays(firstOfMonth.getDayOfWeek().getValue() - 1);

        // 42 jours (6 semaines)
        List<LocalDate> calendarDays = IntStream.range(0, 42)
                .mapToObj(i -> startCalendar.plusDays(i))
                .collect(Collectors.toList());

        // 👉 transformer en semaines (liste de listes)
        List<List<LocalDate>> weeks = IntStream.range(0, 6)
                .mapToObj(i -> calendarDays.subList(i * 7, i * 7 + 7))
                .collect(Collectors.toList());

        // fetch events pour la période affichée
        LocalDateTime rangeStart = startCalendar.atStartOfDay();
        LocalDateTime rangeEnd = startCalendar.plusDays(42).atTime(23, 59, 59);
        List<PlaningEntretien> events = repo.findByDateDebutBetween(rangeStart, rangeEnd);

        // group by date (LocalDate)
        Map<LocalDate, List<PlaningEntretien>> grouped = events.stream()
                .collect(Collectors.groupingBy(e -> e.getDateDebut().toLocalDate()));

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        Map<LocalDate, String> summaryMap = new HashMap<>();
        Map<LocalDate, String> tooltipMap = new HashMap<>();

        for (Map.Entry<LocalDate, List<PlaningEntretien>> entry : grouped.entrySet()) {
            LocalDate date = entry.getKey();
            List<PlaningEntretien> list = entry.getValue().stream()
                    .sorted(Comparator.comparing(PlaningEntretien::getDateDebut))
                    .collect(Collectors.toList());

            String startStr = timeFmt.format(list.get(0).getDateDebut());
            String endStr = timeFmt.format(list.get(list.size() - 1).getDateDebut());
            summaryMap.put(date, "Entretiens prévus de " + startStr + " à " + endStr);

            String tooltipHtml = list.stream().map(ev -> {
                String h1 = timeFmt.format(ev.getDateDebut());
                String h2 = ev.getDateFin() != null ? timeFmt.format(ev.getDateFin()) : "";
                String nom = ev.getCandidat() != null ? (ev.getCandidat().getNom() + " " + ev.getCandidat().getPrenom()) : "—";
                String poste = "—";
                try {
                    if (ev.getCandidat() != null && ev.getCandidat().getAnnonce() != null) {
                        poste = ev.getCandidat().getAnnonce().getProfil();
                    }
                } catch (Exception ex) {
                    poste = "—";
                }
                return h1 + (h2.isEmpty() ? "" : " à " + h2) + " — " + nom + " — " + poste;
            }).collect(Collectors.joining("<br/>"));
            tooltipMap.put(date, tooltipHtml);
        }

        // Ajout au modèle (⚠️ noms utilisés dans le template)
        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("monthName", ym.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.FRANCE));
        model.addAttribute("weeks", weeks);
        model.addAttribute("currentMonth", ym);
        model.addAttribute("summaryMap", summaryMap);
        model.addAttribute("tooltipMap", tooltipMap);

        log.debug("Calendrier préparé : weeks={}, summaryMapSize={}, tooltipMapSize={}",
                weeks.size(), summaryMap.size(), tooltipMap.size());

        return "calendrier/calendrier";
    }
}
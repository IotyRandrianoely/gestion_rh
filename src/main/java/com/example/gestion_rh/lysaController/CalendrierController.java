package com.example.gestion_rh.lysaController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.gestion_rh.lysaRepository.PlaningEntretienRepository;
import com.example.gestion_rh.lysaModel.PlaningEntretien;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/calendrier")
public class CalendrierController {

    private final PlaningEntretienRepository repo;

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
        LocalDate lastOfMonth = ym.atEndOfMonth();

        LocalDate startCalendar = firstOfMonth.minusDays((firstOfMonth.getDayOfWeek().getValue() - 1)); // start Monday
        // build 6 weeks grid (6*7=42)
        List<LocalDate> calendarDays = IntStream.range(0, 42)
                .mapToObj(i -> startCalendar.plusDays(i))
                .collect(Collectors.toList());

        // fetch events for the month range
        LocalDateTime rangeStart = startCalendar.atStartOfDay();
        LocalDateTime rangeEnd = startCalendar.plusDays(42).atTime(23, 59, 59);

        List<PlaningEntretien> events = repo.findByDateDebutBetween(rangeStart, rangeEnd);

        // group by date
        Map<LocalDate, List<PlaningEntretien>> grouped = events.stream()
                .collect(Collectors.groupingBy(e -> e.getDateDebut().toLocalDate()));

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        // prepare summary and tooltip HTML for each date
        Map<LocalDate, String> summaryMap = new HashMap<>();
        Map<LocalDate, String> tooltipMap = new HashMap<>();

        for (Map.Entry<LocalDate, List<PlaningEntretien>> entry : grouped.entrySet()) {
            LocalDate date = entry.getKey();
            List<PlaningEntretien> list = entry.getValue().stream()
                    .sorted(Comparator.comparing(PlaningEntretien::getDateDebut))
                    .collect(Collectors.toList());

            // summary: earliest debut time -> latest debut time
            String startStr = timeFmt.format(list.get(0).getDateDebut());
            String endStr = timeFmt.format(list.get(list.size() - 1).getDateDebut());
            summaryMap.put(date, "Entretiens prévus de " + startStr + " à " + endStr);

            // tooltip: one line per entretien "HH:mm à HH:mm Nom Prenom Profil"
            String tooltipHtml = list.stream().map(ev -> {
                String h1 = timeFmt.format(ev.getDateDebut());
                String h2 = ev.getDateFin() != null ? timeFmt.format(ev.getDateFin()) : "";
                String nom = ev.getCandidat() != null ? (ev.getCandidat().getNom() + " " + ev.getCandidat().getPrenom()) : "—";
                String poste = "—";
                try {
                    if (ev.getCandidat() != null && ev.getCandidat().getAnnonce() != null) {
                        poste = ev.getCandidat().getAnnonce().getProfil();
                    }
                } catch (Exception ex) { poste = "—"; }
                return h1 + (h2.isEmpty() ? "" : " à " + h2) + " — " + nom + " — " + poste;
            }).collect(Collectors.joining("<br/>"));
            tooltipMap.put(date, tooltipHtml);
        }

        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("monthName", ym.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.FRANCE));
        model.addAttribute("calendarDays", calendarDays);
        model.addAttribute("currentMonth", ym);
        model.addAttribute("summaryMap", summaryMap);
        model.addAttribute("tooltipMap", tooltipMap);

        return "calendrier/calendrier";
    }
}
package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.ContratEssaiService;
import com.example.gestion_rh.service.PlaningEntretienService;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import com.example.gestion_rh.model.ContratEssai;
import com.example.gestion_rh.model.PlaningEntretien;
import com.example.gestion_rh.model.Candidat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.net.URLConnection;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/candidats")
public class CandidatController {

    private final CandidatService candidatService;
    private final ContratEssaiService contratEssaiService;
    private final PlaningEntretienService planingEntretienService;

    public CandidatController(CandidatService candidatService, ContratEssaiService contratEssaiService, PlaningEntretienService planingEntretienService) {
        this.candidatService = candidatService;
        this.contratEssaiService = contratEssaiService;
        this.planingEntretienService = planingEntretienService;
    }

    // Liste des candidats
    @GetMapping
    public String list(Model model) {
        model.addAttribute("candidats", candidatService.getAll());
        List<PlaningEntretien> contrats = planingEntretienService.listerPLaningEntretien();
        // Construire une map <idCandidat, PlaningEntretien> (garde le premier contrat si plusieurs)
        Map<Long, PlaningEntretien> contratsMap = contrats.stream()
            .filter(ce -> ce.getCandidat() != null && ce.getCandidat().getId() != null)
            .collect(Collectors.toMap(ce -> ce.getCandidat().getId(), ce -> ce, (a, b) -> a));
        model.addAttribute("contratsEssai", contrats);
        model.addAttribute("contratsMap", contratsMap);
        return "candidats/list";  // renvoie vers /WEB-INF/views/candidats/list.jsp
    }

    // Détail d'un candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        return "candidats/detail"; // renvoie vers /WEB-INF/views/candidats/detail.jsp
    }

    /**
     * Servir les CV placés dans src/main/resources/cv/
     * URL exemple: /candidats/cv/cv_jean.pdf
     */
    @GetMapping("/cv/{filename:.+}")
    public ResponseEntity<Resource> getCv(@PathVariable String filename) throws IOException {
        ClassPathResource file = new ClassPathResource("cv/" + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = URLConnection.guessContentTypeFromName(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                .body(file);
    }

    @GetMapping("planifier_entretien/{id}")
    public String planifierEntretien(@PathVariable Long id) {
        planingEntretienService.creerPlaningEntretienPourCandidat(id);
        return "redirect:/candidats";
    }

    // --- NOUVEAU : Planification manuelle ---
    @GetMapping("planifier_entretien_manual/{id}")
    public String planifierEntretienManual(@PathVariable Long id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        return "candidats/planifier_entretien_manual";
    }

    @PostMapping("planifier_entretien_manual")
    public String enregistrerPlaningManuel(@RequestParam Long candidatId,
                                           @RequestParam String date,
                                           @RequestParam String time) {
        Candidat candidat = candidatService.getById(candidatId);
        if (candidat == null) {
            return "redirect:/candidats";
        }
        LocalDate d = LocalDate.parse(date);      // format yyyy-MM-dd
        LocalTime t = LocalTime.parse(time);      // format HH:mm
        LocalDateTime debut = LocalDateTime.of(d, t);

        PlaningEntretien plan = new PlaningEntretien();
        plan.setCandidat(candidat);
        plan.setDateDebut(debut);
        plan.setDateFin(debut.plusHours(1)); // par défaut 1h

        planingEntretienService.creerPlaningEntretien(plan);

        candidat.setEstPropose(true);
        candidatService.save(candidat);

        return "redirect:/candidats";
    }
}
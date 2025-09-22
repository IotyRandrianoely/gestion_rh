package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.ContratEssaiService;
import com.example.gestion_rh.service.PlaningEntretienService;
import com.example.gestion_rh.service.HistoriqueScoreService;

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

// <-- Ajoute ces imports ----------
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
// ----------------------------------
import java.util.stream.Collectors;
import java.util.Set;

@Controller
@RequestMapping("/candidats")
public class CandidatController {

    private final CandidatService candidatService;
    private final ContratEssaiService contratEssaiService;
    private final PlaningEntretienService planingEntretienService;
    private final HistoriqueScoreService historiqueScoreService; // <--- NEW

    public CandidatController(CandidatService candidatService,
                              ContratEssaiService contratEssaiService,
                              PlaningEntretienService planingEntretienService,
                              HistoriqueScoreService historiqueScoreService) { // <--- NEW param
        this.candidatService = candidatService;
        this.contratEssaiService = contratEssaiService;
        this.planingEntretienService = planingEntretienService;
        this.historiqueScoreService = historiqueScoreService; // <--- assign
    }

    // Liste des candidats (avec filtres)
    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Double minScore,
                       @RequestParam(required = false) String profil,
                       @RequestParam(required = false) String propose) {

        List<Candidat> allCandidats = candidatService.getAll();
        if (allCandidats == null) allCandidats = List.of();

        // Construire scoresMap pour tous les candidats
        Map<Integer, Double> scoresMap = new java.util.HashMap<>();
        for (Candidat c : allCandidats) {
            try {
                Integer annonceId = c.getAnnonce() != null ? c.getAnnonce().getId() : null;
                Double score = (annonceId != null) ? historiqueScoreService.getLatestScoreFor((long) c.getId(), annonceId) : null;
                scoresMap.put(c.getId(), score);
            } catch (Exception ex) {
                scoresMap.put(c.getId(), null);
            }
        }

        // Construire contratsMap
        List<PlaningEntretien> contrats = planingEntretienService.listerPLaningEntretien();
        Map<Integer, PlaningEntretien> contratsMap = new java.util.HashMap<>();
        if (contrats != null) {
            for (PlaningEntretien pe : contrats) {
                if (pe != null && pe.getCandidat() != null && pe.getCandidat().getId() != null) {
                    int cid = pe.getCandidat().getId();
                    if (!contratsMap.containsKey(cid)) contratsMap.put(cid, pe);
                }
            }
        }

        // Liste distincte des profils pour le select
        Set<String> profils = allCandidats.stream()
                .map(c -> c.getAnnonce() != null ? c.getAnnonce().getProfil() : null)
                .filter(p -> p != null && !p.isBlank())
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));

        // Appliquer filtres
        List<Candidat> filtered = allCandidats.stream().filter(c -> {
            // filter by name (contains nom or prenom)
            if (name != null && !name.isBlank()) {
                String low = name.trim().toLowerCase();
                String nom = c.getNom() != null ? c.getNom().toLowerCase() : "";
                String prenom = c.getPrenom() != null ? c.getPrenom().toLowerCase() : "";
                if (!nom.contains(low) && !prenom.contains(low)) return false;
            }
            // filter by profil
            if (profil != null && !profil.isBlank()) {
                if (c.getAnnonce() == null || c.getAnnonce().getProfil() == null) return false;
                if (!profil.equals(c.getAnnonce().getProfil())) return false;
            }
            // filter by propose status
            if ("yes".equalsIgnoreCase(propose)) {
                if (!Boolean.TRUE.equals(c.getEstPropose())) return false;
            } else if ("no".equalsIgnoreCase(propose)) {
                if (Boolean.TRUE.equals(c.getEstPropose())) return false;
            }
            // filter by minScore
            if (minScore != null) {
                Double s = scoresMap.get(c.getId());
                if (s == null || s < minScore) return false;
            }
            return true;
        }).collect(Collectors.toList());

        // Trier : proposé first, puis ceux qui ont un planning, puis dateDebut asc, puis score desc, puis nom
        filtered.sort((a, b) -> {
            boolean propA = Boolean.TRUE.equals(a.getEstPropose());
            boolean propB = Boolean.TRUE.equals(b.getEstPropose());
            if (propA != propB) return propA ? -1 : 1;

            boolean hasA = contratsMap.get(a.getId()) != null;
            boolean hasB = contratsMap.get(b.getId()) != null;
            if (hasA != hasB) return hasA ? -1 : 1;

            PlaningEntretien pa = contratsMap.get(a.getId());
            PlaningEntretien pb = contratsMap.get(b.getId());
            if (hasA && hasB) {
                if (pa != null && pb != null && pa.getDateDebut() != null && pb.getDateDebut() != null) {
                    int cmpDate = pa.getDateDebut().compareTo(pb.getDateDebut());
                    if (cmpDate != 0) return cmpDate;
                } else if (pa != null && pa.getDateDebut() != null) {
                    return -1;
                } else if (pb != null && pb.getDateDebut() != null) {
                    return 1;
                }
            }

            Double sa = scoresMap.get(a.getId());
            Double sb = scoresMap.get(b.getId());
            double va = sa != null ? sa : Double.NEGATIVE_INFINITY;
            double vb = sb != null ? sb : Double.NEGATIVE_INFINITY;
            int cmpScore = Double.compare(vb, va);
            if (cmpScore != 0) return cmpScore;

            String na = a.getNom() != null ? a.getNom() : "";
            String nb = b.getNom() != null ? b.getNom() : "";
            return na.compareToIgnoreCase(nb);
        });

        model.addAttribute("candidats", filtered);
        model.addAttribute("contratsMap", contratsMap);
        model.addAttribute("scoresMap", scoresMap);
        model.addAttribute("profils", profils);

        // conserver valeurs des filtres pour le formulaire
        model.addAttribute("filterName", name);
        model.addAttribute("filterMinScore", minScore);
        model.addAttribute("filterProfil", profil);
        model.addAttribute("filterPropose", propose);

        return "candidats/list";
    }

    // Détail d'un candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable int id, Model model) {
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
    public String planifierEntretien(@PathVariable int id) {
        planingEntretienService.creerPlaningEntretienPourCandidat(id);
        return "redirect:/candidats";
    }

    // --- NOUVEAU : Planification manuelle ---
    @GetMapping("planifier_entretien_manual/{id}")
    public String planifierEntretienManual(@PathVariable int id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        return "candidats/planifier_entretien_manual";
    }

    @PostMapping("planifier_entretien_manual")
    public String enregistrerPlaningManuel(@RequestParam int candidatId,
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

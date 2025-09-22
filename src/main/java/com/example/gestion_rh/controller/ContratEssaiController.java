package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import com.example.gestion_rh.repository.CandidatRepository;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.model.ContratEssai;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.repository.PosteRepository;
import com.example.gestion_rh.service.ContratEssaiService;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Controller
@RequestMapping("/contratEssai")
public class ContratEssaiController {

    private final ContratEssaiService service;
    private final PosteRepository posteRepo;
    private final CandidatRepository candidatRepo;
    private final SpringTemplateEngine templateEngine;

    public ContratEssaiController(ContratEssaiService service,
                                  PosteRepository posteRepo,
                                  CandidatRepository candidatRepo,
                                  SpringTemplateEngine templateEngine) {
        this.service = service;
        this.posteRepo = posteRepo;
        this.candidatRepo = candidatRepo;
        this.templateEngine = templateEngine;
    }

    // Affiche le formulaire (inchangé)
    @GetMapping("/contratEssai")
    public String showForm(@RequestParam(required = false) Long candidateId, Model model) {
        model.addAttribute("postes", posteRepo.findAll());
        if (candidateId != null) {
            Optional<Candidat> cand = candidatRepo.findById(candidateId);
            cand.ifPresent(c -> model.addAttribute("candidate", c));
            model.addAttribute("candidateId", candidateId);
        }
        return "contratEssai/contratEssai";
    }

    // Traite la soumission : sauvegarde puis renvoie le PDF en téléchargement
    @PostMapping("/save")
    public ResponseEntity<byte[]> saveFromForm(@RequestParam(required = false) Long candidateId,
                                               @RequestParam(required = false) Long posteId,
                                               @RequestParam String dateDebut,
                                               @RequestParam Integer duree,
                                               @RequestParam Double salaire,
                                               RedirectAttributes redirectAttrs) {

        ContratEssai contrat = new ContratEssai();

        // associer le candidat si présent
        if (candidateId != null) {
            Optional<Candidat> cand = candidatRepo.findById(candidateId);
            if (cand.isPresent()) {
                contrat.setCandidat(cand.get());
            } else {
                redirectAttrs.addFlashAttribute("error", "Candidat introuvable");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        // si posteId non fourni, tenter de le déduire depuis le candidat -> annonce -> poste
        if (posteId == null) {
            if (contrat.getCandidat() != null && contrat.getCandidat().getAnnonce() != null) {
                try {
                    // plusieurs variantes possibles selon le modèle Annonce : poste (entity) ou idPoste (Long/Integer)
                    Object maybePoste = contrat.getCandidat().getAnnonce().getPoste();
                    if (maybePoste instanceof Poste) {
                        Long id = ((Poste) maybePoste).getId();
                        if (id != null) posteId = id;
                    } else {
                        // si Annonce expose idPoste (Integer/Long), gérer le cas
                        try {
                            // reflection fallback to support different model variants
                            java.lang.reflect.Method m = contrat.getCandidat().getAnnonce().getClass().getMethod("getIdPoste");
                            Object v = m.invoke(contrat.getCandidat().getAnnonce());
                            if (v instanceof Number) posteId = ((Number) v).longValue();
                        } catch (NoSuchMethodException ignore) {
                            // rien à faire si la méthode n'existe pas
                        }
                    }
                } catch (Exception ex) {
                    // ignore and continue to validation below
                }
            }
        }

        // validation posteId
        if (posteId == null) {
            redirectAttrs.addFlashAttribute("error", "Veuillez sélectionner un poste ou utilisez la page détail du candidat.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Poste> p = posteRepo.findById(posteId);
        if (p.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Poste introuvable");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        contrat.setPoste(p.get());

        // parsing de la date : robust à yyyy-MM-dd ou yyyy-MM-ddTHH:mm[:ss]
        try {
            LocalDateTime dt;
            try {
                dt = LocalDateTime.parse(dateDebut);
            } catch (Exception ex) {
                // fallback : date-only -> start of day
                dt = LocalDate.parse(dateDebut).atStartOfDay();
            }
            contrat.setDateDebut(dt);

            // --- NEW: calculer et définir dateFin = dateDebut + duree jours ---
            if (duree != null) {
                LocalDateTime end = dt.plusDays(duree);
                try {
                    // essayer setDateFin(LocalDateTime)
                    java.lang.reflect.Method m = contrat.getClass().getMethod("setDateFin", LocalDateTime.class);
                    m.invoke(contrat, end);
                } catch (NoSuchMethodException ns) {
                    try {
                        // essayer setDateFin(LocalDate)
                        java.lang.reflect.Method m2 = contrat.getClass().getMethod("setDateFin", LocalDate.class);
                        m2.invoke(contrat, end.toLocalDate());
                    } catch (NoSuchMethodException ns2) {
                        // pas de setter compatible trouvé — ignorer (mais garder sauvegarde)
                    }
                } catch (Exception e) {
                    // ignore invocation errors
                }
            }
            // --- END NEW ---
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", "Date de début invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        contrat.setDuree(duree);
        contrat.setSalaire(salaire);
        // contrat.setConditions(conditions);

        // Sauvegarde dans la base
        ContratEssai saved = service.creerContrat(contrat);

        // Préparer le HTML via Thymeleaf
        Context ctx = new Context();
        ctx.setVariable("contrat", saved);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ctx.setVariable("dateNow", LocalDate.now().format(dtf));
        String html = templateEngine.process("contratEssai/contratEssai-pdf", ctx);

        // Générer le PDF
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            byte[] pdfBytes = os.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "contrat_essai_" + saved.getId() + ".pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception ex) {
            // si erreur génération PDF, renvoyer redirect simple (ou erreur 500)
            redirectAttrs.addFlashAttribute("error", "Erreur lors de la génération du PDF");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
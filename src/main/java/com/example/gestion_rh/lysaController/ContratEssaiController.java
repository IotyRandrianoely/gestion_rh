package com.example.gestion_rh.lysaController;

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

import com.example.gestion_rh.lysaModel.ContratEssai;
import com.example.gestion_rh.lysaModel.Candidat;
import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.lysaService.ContratEssaiService;
import com.example.gestion_rh.repository.PosteRepository;
import com.example.gestion_rh.lysaRepository.CandidatRepository;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
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
                                               @RequestParam Long posteId,
                                               @RequestParam String dateDebut,
                                               @RequestParam Integer duree,
                                               @RequestParam Double salaire,
                                               @RequestParam(required = false) String conditions,
                                               RedirectAttributes redirectAttrs) {

        ContratEssai contrat = new ContratEssai();

        if (candidateId != null) {
            Optional<Candidat> cand = candidatRepo.findById(candidateId);
            if (cand.isPresent()) {
                contrat.setCandidat(cand.get());
            } else {
                redirectAttrs.addFlashAttribute("error", "Candidat introuvable");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        Optional<Poste> p = posteRepo.findById(posteId);
        if (p.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Poste introuvable");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        contrat.setPoste(p.get());

        try {
            contrat.setDateDebut(LocalDate.parse(dateDebut));
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", "Date de début invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        contrat.setDuree(duree);
        contrat.setSalaire(salaire);
        contrat.setConditions(conditions);

        // Sauvegarde dans la base
        ContratEssai saved = service.creerContrat(contrat);

        // Préparer le HTML via Thymeleaf
        Context ctx = new Context();
        ctx.setVariable("contrat", saved);
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
package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.DiplomeService;
import com.example.gestion_rh.service.FileStorageService;

@Controller
@RequestMapping("/client/candidats")
public class CandidatClientController {

    private final CandidatService candidatService;
    private final AnnonceService annonceService;
    private final DiplomeService diplomeService;
    private final FileStorageService fileStorageService;

    public CandidatClientController(CandidatService candidatService, AnnonceService annonceService, 
                                   DiplomeService diplomeService, FileStorageService fileStorageService) {
        this.candidatService = candidatService;
        this.annonceService = annonceService;
        this.diplomeService = diplomeService;
        this.fileStorageService = fileStorageService;
    }

    // Liste des candidats avec filtre par annonce
    // @GetMapping
    // public String list(Model model, @RequestParam(required = false) Integer annonceId) {
    //     try {
    //         if (annonceId != null) {
    //             model.addAttribute("candidats", candidatService.getByAnnonceId(annonceId));
    //         } else {
    //             model.addAttribute("candidats", candidatService.getAll());
    //         }
    //         model.addAttribute("annonces", annonceService.getAll());
    //         model.addAttribute("selectedAnnonce", annonceId);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         model.addAttribute("error", "Erreur lors du chargement des candidats: " + e.getMessage());
    //     }
    //     return "candidats/list";
    // }

    // Formulaire de candidature
    @GetMapping("/postuler")
    public String formCandidature(Model model, @RequestParam(required = false) Integer annonceId) {
        try {
            Candidat candidat = new Candidat();
            model.addAttribute("candidat", candidat);
            model.addAttribute("annonces", annonceService.getAll());
            model.addAttribute("diplomes", diplomeService.getAll()); // *** NOUVEAU ***
            model.addAttribute("selectedAnnonceId", annonceId);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement du formulaire: " + e.getMessage());
        }
        return "candidats/form";
    }

    // Sauvegarde candidature avec upload de CV
    @PostMapping("/postuler")
    public String save(@ModelAttribute Candidat candidat, 
                      @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
                      RedirectAttributes redirectAttributes) {
        try {
            // Validation des données obligatoires existantes...
            if (candidat.getNom() == null || candidat.getNom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le nom est obligatoire");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getPrenom() == null || candidat.getPrenom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le prénom est obligatoire");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getEmail() == null || candidat.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "L'email est obligatoire");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getAnnonce() == null || candidat.getAnnonce().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner une annonce");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getAge() == null || candidat.getAge() < 16 || candidat.getAge() > 70) {
                redirectAttributes.addFlashAttribute("error", "L'âge doit être entre 16 et 70 ans");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getGenre() == null) {
                redirectAttributes.addFlashAttribute("error", "Le genre est obligatoire");
                return "redirect:/candidats/postuler";
            }

            if (candidat.getAnneesExperience() == null || candidat.getAnneesExperience() < 0) {
                redirectAttributes.addFlashAttribute("error", "Les années d'expérience sont obligatoires");
                return "redirect:/candidats/postuler";
            }

            // *** VALIDATION DU DIPLOME ***
            if (candidat.getDiplome() == null || candidat.getDiplome().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner un diplôme");
                return "redirect:/candidats/postuler";
            }

            // Gestion de l'upload du CV
            if (cvFile != null && !cvFile.isEmpty()) {
                try {
                    String fileName = fileStorageService.storeFile(cvFile);
                    candidat.setCv(fileName);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("error", "Erreur lors de l'upload du CV: " + e.getMessage());
                    return "redirect:/candidats/postuler";
                }
            }

            // Vérification d'éligibilité
            if (!candidatService.isEligible(candidat)) {
                String ineligibilityMessage = candidatService.getIneligibilityMessage(candidat);
                redirectAttributes.addFlashAttribute("error", ineligibilityMessage);
                redirectAttributes.addFlashAttribute("notEligible", true);
                return "redirect:/candidats/postuler";
            }

            // Sauvegarde si éligible
            Candidat savedCandidat = candidatService.save(candidat);

            if (savedCandidat != null && savedCandidat.getId() != null) {
                redirectAttributes.addFlashAttribute("success", "Candidature enregistrée avec succès ! Vous êtes éligible pour cette annonce.");
                return "redirect:/candidats?annonceId=" + candidat.getAnnonce().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement");
                return "redirect:/candidats/postuler";
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur technique: " + e.getMessage());
            return "redirect:/candidats/postuler";
        }
    }

    // Détail candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        try {
            Candidat candidat = candidatService.getById(id);
            if (candidat == null) {
                model.addAttribute("error", "Candidat non trouvé");
                return "redirect:/candidats";
            }
            model.addAttribute("candidat", candidat);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }
        return "candidats/detail";
    }
}
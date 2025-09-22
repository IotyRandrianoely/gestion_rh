package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CandidatService;
import com.example.gestion_rh.service.DiplomeService;
import com.example.gestion_rh.service.FileStorageService;
import com.example.gestion_rh.service.ContratEssaiService;

@Controller
@RequestMapping("/admin/candidats")
public class CandidatAdminController {

    private final CandidatService candidatService;
    private final AnnonceService annonceService;
    private final DiplomeService diplomeService;
    private final ContratEssaiService contratEssaiService;
    private FileStorageService fileStorageService;

    public CandidatAdminController(CandidatService candidatService,
                                   AnnonceService annonceService,
                                   DiplomeService diplomeService,
                                   ContratEssaiService contratEssaiService,
                                   FileStorageService fileStorageService) {
        this.candidatService = candidatService;
        this.annonceService = annonceService;
        this.diplomeService = diplomeService;
        this.contratEssaiService = contratEssaiService;
        this.fileStorageService = fileStorageService;
    }

    // Liste des candidats
    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("candidats", candidatService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }
        return "admin/candidats/index";
    }

    // Formulaire nouveau candidat
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("candidat", new Candidat());
        model.addAttribute("annonces", annonceService.getAll());
        model.addAttribute("diplomes", diplomeService.getAll()); // *** NOUVEAU ***
        return "admin/candidats/form";
    }

    // Formulaire édition candidat
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        try {
            Candidat candidat = candidatService.getById(id);
            if (candidat == null) {
                model.addAttribute("error", "Candidat non trouvé");
                return "redirect:/admin/candidats";
            }
            model.addAttribute("candidat", candidat);
            model.addAttribute("annonces", annonceService.getAll());
            model.addAttribute("diplomes", diplomeService.getAll()); // *** NOUVEAU ***
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }
        return "admin/candidats/form";
    }

    // Sauvegarde
    @PostMapping
    public String save(@ModelAttribute Candidat candidat,
            @RequestParam(name = "annonce.id", required = false) Integer annonceId,
            @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation des champs obligatoires
            if (candidat.getNom() == null || candidat.getNom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le nom est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getPrenom() == null || candidat.getPrenom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le prénom est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getEmail() == null || candidat.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "L'email est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getAnnonce() == null || candidat.getAnnonce().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner une annonce");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getAge() == null || candidat.getAge() < 16 || candidat.getAge() > 70) {
                redirectAttributes.addFlashAttribute("error", "L'âge doit être entre 16 et 70 ans");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getGenre() == null) {
                redirectAttributes.addFlashAttribute("error", "Le genre est obligatoire");
                return "redirect:/admin/candidats/new";
            }

            if (candidat.getAnneesExperience() == null || candidat.getAnneesExperience() < 0) {
                redirectAttributes.addFlashAttribute("error", "Les années d'expérience sont obligatoires");
                return "redirect:/admin/candidats/new";
            }

            // *** VALIDATION DU DIPLOME ***
            if (candidat.getDiplome() == null || candidat.getDiplome().getId() == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner un diplôme");
                return "redirect:/admin/candidats/new";
            }

            System.out.println("=== DEBUG ADMIN CANDIDAT ===");
            System.out.println("Candidat ID: " + candidat.getId());
            System.out.println("Nom: " + candidat.getNom());
            System.out
                    .println("Annonce ID: " + (candidat.getAnnonce() != null ? candidat.getAnnonce().getId() : "null"));
            System.out
                    .println("Diplome ID: " + (candidat.getDiplome() != null ? candidat.getDiplome().getId() : "null"));

            // Gestion de l'upload du CV - AMÉLIORATION
            if (cvFile != null && !cvFile.isEmpty()) {
                try {
                    System.out.println("=== UPLOAD CV ===");
                    System.out.println("Fichier: " + cvFile.getOriginalFilename());
                    System.out.println("Taille: " + cvFile.getSize());
                    System.out.println("Type: " + cvFile.getContentType());

                    String fileName = fileStorageService.storeFile(cvFile, FileStorageService.FileType.DOCUMENT);
                    candidat.setCv(fileName);

                    System.out.println("CV sauvegardé sous: " + fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", "Erreur lors de l'upload du CV: " + e.getMessage());
                    return "redirect:/admin/candidats/new";
                }
            }

            Candidat savedCandidat = candidatService.save(candidat);
            System.out.println(
                    "Candidat sauvegardé avec CV: " + (savedCandidat != null ? savedCandidat.getCv() : "null"));

            if (savedCandidat != null && savedCandidat.getId() != null) {
                redirectAttributes.addFlashAttribute("success", "Candidat sauvegardé avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("error", "Erreur lors de la sauvegarde");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde admin: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur technique: " + e.getMessage());
        }

        return "redirect:/admin/candidats";
    }

    // Détail candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        try {
            Candidat candidat = candidatService.getById(id);
            if (candidat == null) {
                model.addAttribute("error", "Candidat non trouvé");
                return "redirect:/admin/candidats";
            }
            model.addAttribute("candidat", candidat);
            // --- calculs contrats ---
            long contratCount = 0L;
            int totalDuree = 0;
            try {
                if (candidat.getId() != null) {
                    contratCount = contratEssaiService.countByCandidatId(candidat.getId().longValue());
                    totalDuree = contratEssaiService.sumDureeByCandidatId(candidat.getId().longValue());
                }
            } catch (Exception ex) {
                // ne pas bloquer l'affichage si le service n'est pas disponible
            }
            int remaining = Math.max(0, 180 - totalDuree);
            boolean canPropose = (contratCount == 0L) && remaining > 0;
            boolean canRenew = (contratCount == 1L) && remaining > 0;

            model.addAttribute("contratCount", contratCount);
            model.addAttribute("totalDureeContrats", totalDuree);
            model.addAttribute("remainingDuree", remaining);
            model.addAttribute("canPropose", canPropose);
            model.addAttribute("canRenew", canRenew);
            model.addAttribute("maxRenewDuration", remaining);
            // --- fin calculs ---
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors du chargement: " + e.getMessage());
        }
        return "admin/candidats/detail";
    }

    // Suppression candidat
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            candidatService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Candidat supprimé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/admin/candidats";
    }
}
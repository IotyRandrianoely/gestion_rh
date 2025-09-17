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

import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.service.CandidatService;

@Controller
@RequestMapping("/admin/candidats")
public class CandidatAdminController {

    private final CandidatService candidatService;
    private final AnnonceService annonceService;

    public CandidatAdminController(CandidatService candidatService, AnnonceService annonceService) {
        this.candidatService = candidatService;
        this.annonceService = annonceService;
    }

    // Liste admin
    @GetMapping
    public String index(Model model) {
        model.addAttribute("candidats", candidatService.getAll());
        return "admin/candidats/index";
    }

    // Formulaire ajout
    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("candidat", new Candidat());
        model.addAttribute("annonces", annonceService.getAll());
        return "admin/candidats/form";
    }

    // Formulaire édition
    @GetMapping("/edit/{id}")
    public String formEdit(@PathVariable Integer id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        model.addAttribute("annonces", annonceService.getAll());
        return "admin/candidats/form";
    }

    // Sauvegarde
    @PostMapping
    public String save(@ModelAttribute Candidat candidat,
            @RequestParam(name = "annonce.id", required = false) Integer annonceId,
            RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== DEBUG ADMIN CANDIDAT ===");
            System.out.println("Candidat ID: " + candidat.getId());
            System.out.println("Nom: " + candidat.getNom());
            System.out.println("Prenom: " + candidat.getPrenom());
            System.out.println("Email: " + candidat.getEmail());
            System.out.println("Annonce ID reçu: " + annonceId);

            // Validation des données obligatoires
            if (candidat.getNom() == null || candidat.getNom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le nom est obligatoire");
                return candidat.getId() == null ? "redirect:/admin/candidats/new"
                        : "redirect:/admin/candidats/edit/" + candidat.getId();
            }

            if (candidat.getPrenom() == null || candidat.getPrenom().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le prénom est obligatoire");
                return candidat.getId() == null ? "redirect:/admin/candidats/new"
                        : "redirect:/admin/candidats/edit/" + candidat.getId();
            }

            if (candidat.getEmail() == null || candidat.getEmail().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "L'email est obligatoire");
                return candidat.getId() == null ? "redirect:/admin/candidats/new"
                        : "redirect:/admin/candidats/edit/" + candidat.getId();
            }

            // Gérer la relation avec l'annonce
            Integer finalAnnonceId = annonceId;
            if (finalAnnonceId == null && candidat.getAnnonce() != null) {
                finalAnnonceId = candidat.getAnnonce().getId();
            }

            if (finalAnnonceId == null) {
                redirectAttributes.addFlashAttribute("error", "Veuillez sélectionner une annonce");
                return candidat.getId() == null ? "redirect:/admin/candidats/new"
                        : "redirect:/admin/candidats/edit/" + candidat.getId();
            }

            // Récupérer l'annonce depuis la base de données
            Annonce annonce = annonceService.getById(finalAnnonceId);
            if (annonce == null) {
                redirectAttributes.addFlashAttribute("error", "Annonce non trouvée");
                return candidat.getId() == null ? "redirect:/admin/candidats/new"
                        : "redirect:/admin/candidats/edit/" + candidat.getId();
            }

            candidat.setAnnonce(annonce);
            System.out.println("Annonce assignée: " + annonce.getId() + " - " + annonce.getProfil());

            Candidat savedCandidat = candidatService.save(candidat);
            System.out.println(
                    "Candidat sauvegardé avec ID: " + (savedCandidat != null ? savedCandidat.getId() : "null"));

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

    // Suppression
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

    // Détail candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        return "admin/candidats/detail";
    }
}
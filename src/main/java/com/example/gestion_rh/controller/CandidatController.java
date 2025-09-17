package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.gestion_rh.service.CandidatService;

@Controller
@RequestMapping("/candidats")
public class CandidatController {

    private final CandidatService candidatService;

    public CandidatController(CandidatService candidatService) {
        this.candidatService = candidatService;
    }

    // Liste des candidats
    @GetMapping
    public String list(Model model) {
        model.addAttribute("candidats", candidatService.getAll());
        return "candidats/list";  // renvoie vers /WEB-INF/views/candidats/list.jsp
    }

    // Détail d'un candidat
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("candidat", candidatService.getById(id));
        return "candidats/detail"; // renvoie vers /WEB-INF/views/candidats/detail.jsp
    }
}
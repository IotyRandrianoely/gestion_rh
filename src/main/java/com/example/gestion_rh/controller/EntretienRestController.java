package com.example.gestion_rh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gestion_rh.service.PlaningEntretienService;

@RestController
@RequestMapping("/api/entretiens")
public class EntretienRestController {

    private final PlaningEntretienService planingEntretienService;

    public EntretienRestController(PlaningEntretienService planingEntretienService) {
        this.planingEntretienService = planingEntretienService;
    }

    @PostMapping("/{id}/annuler")
    public ResponseEntity<?> annulerEntretien(@PathVariable Long id) {
        try {
            planingEntretienService.annulerEntretien(id, "Entretien annulé depuis le calendrier");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'annulation : " + e.getMessage());
        }
    }
}
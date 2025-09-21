package com.example.gestion_rh.controller;

import org.springframework.web.bind.annotation.*;

import com.example.gestion_rh.model.ContratEssai;
import com.example.gestion_rh.service.ContratEssaiService;

import java.util.List;

@RestController
@RequestMapping("/api/contrats-essai")
public class ContratEssaiController {

    private final ContratEssaiService service;

    public ContratEssaiController(ContratEssaiService service) {
        this.service = service;
    }

    @PostMapping
    public ContratEssai creerContrat(@RequestBody ContratEssai contrat) {
        return service.creerContrat(contrat);
    }

    @GetMapping
    public List<ContratEssai> listerContrats() {
        return service.listerContrats();
    }

    @GetMapping("/{id}")
    public ContratEssai getContrat(@PathVariable Long id) {
        return service.getContrat(id);
    }

    @PutMapping("/{id}")
    public ContratEssai mettreAJour(@PathVariable Long id, @RequestBody ContratEssai contrat) {
        return service.mettreAJourContrat(id, contrat);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        service.supprimerContrat(id);
    }
}
package com.example.gestion_rh.service;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.ContratEssai;
import com.example.gestion_rh.repository.ContratEssaiRepository;

import java.util.List;

@Service
public class ContratEssaiService {

    private final ContratEssaiRepository repository;

    public ContratEssaiService(ContratEssaiRepository repository) {
        this.repository = repository;
    }

    public ContratEssai creerContrat(ContratEssai contrat) {
        return repository.save(contrat);
    }

    public List<ContratEssai> listerContrats() {
        return repository.findAll();
    }

    public ContratEssai getContrat(Long id) {
        return repository.findById(id).orElse(null);
    }

    public ContratEssai mettreAJourContrat(Long id, ContratEssai contrat) {
        contrat.setId(id);
        return repository.save(contrat);
    }

    public void supprimerContrat(Long id) {
        repository.deleteById(id);
    }

    /**
     * Nombre de contrats d'essai pour un candidat donné
     */
    public long countByCandidatId(Long candidatId) {
        return repository.countByCandidatId(candidatId);
    }

    /**
     * Somme des durees (en jours) pour tous les contrats du candidat.
     * Retourne 0 si aucun contrat.
     */
    public int sumDureeByCandidatId(Long candidatId) {
        return repository.sumDureeByCandidatId(candidatId);
    }
}
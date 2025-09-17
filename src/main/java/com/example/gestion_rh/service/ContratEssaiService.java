package com.example.gestion_rh.Service;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.Model.ContratEssai;
import com.example.gestion_rh.lysaRepository.ContratEssaiRepository;

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
}
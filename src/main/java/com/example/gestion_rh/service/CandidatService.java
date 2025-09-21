package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Candidat;
import com.example.gestion_rh.repository.CandidatRepository;

@Service
public class CandidatService {
    private final CandidatRepository repo;

    public CandidatService(CandidatRepository repo) {
        this.repo = repo;
    }

    public List<Candidat> getAll() { return repo.findAll(); }

    public Candidat getById(Long id) { return repo.findById(id).orElse(null); }

    public Candidat save(Candidat c) { return repo.save(c); }

    public void delete(Long id) { repo.deleteById(id); }
}

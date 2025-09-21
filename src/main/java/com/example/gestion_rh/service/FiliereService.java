package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Filiere;
import com.example.gestion_rh.repository.FiliereRepository;

@Service
public class FiliereService {
    private final FiliereRepository repo;

    public FiliereService(FiliereRepository repo) {
        this.repo = repo;
    }

    public List<Filiere> getAll() { return repo.findAll(); }

    public Filiere getById(Integer id) { return repo.findById(id).orElse(null); }
}

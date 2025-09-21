package com.example.gestion_rh.service;

import com.example.gestion_rh.model.Diplome;
import com.example.gestion_rh.repository.DiplomeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiplomeService {
    private final DiplomeRepository repo;

    public DiplomeService(DiplomeRepository repo) {
        this.repo = repo;
    }

    public List<Diplome> getAll() { return repo.findAll(); }

    public Diplome getById(Integer id) { return repo.findById(id).orElse(null); }
}

package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.CritereRech;
import com.example.gestion_rh.repository.CritereRechRepository;

@Service
public class CritereRechService {
    private final CritereRechRepository repo;

    public CritereRechService(CritereRechRepository repo) {
        this.repo = repo;
    }

    public List<CritereRech> getAll() { return repo.findAll(); }

    public CritereRech getById(Integer id) { return repo.findById(id).orElse(null); }

    public CritereRech save(CritereRech c) { return repo.save(c); }

    public void delete(Integer id) { repo.deleteById(id); }
}

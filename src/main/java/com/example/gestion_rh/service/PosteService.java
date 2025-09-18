package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Poste;
import com.example.gestion_rh.repository.PosteRepository;

@Service
public class PosteService {
    private final PosteRepository repo;

    public PosteService(PosteRepository repo) {
        this.repo = repo;
    }
    public Poste getById(Long id) { return repo.findById(id).orElse(null); }

    public List<Poste> getAll() { return repo.findAll(); }
    public Poste save(Poste poste) { return repo.save(poste); }
    public void delete(Long id) { repo.deleteById(id); }
}
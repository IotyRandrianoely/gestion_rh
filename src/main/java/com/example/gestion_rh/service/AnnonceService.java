package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Annonce;
import com.example.gestion_rh.repository.AnnonceRepository;

@Service
public class AnnonceService {
    private final AnnonceRepository repo;

    public AnnonceService(AnnonceRepository repo) {
        this.repo = repo;
    }

    // >> ICI : List<Annonce>, pas List<Object>
    public List<Annonce> getAll() {
        return repo.findAll();
    }

    public Annonce getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Annonce save(Annonce a) {
        return repo.save(a);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
      public Integer getFiliereIdByAnnonceId(Integer annonceId) {
        Annonce annonce = getById(annonceId);
        if (annonce != null && annonce.getCritereRech() != null && annonce.getCritereRech().getFiliere() != null) {
            return annonce.getCritereRech().getFiliere().getId();
        }
        return null;
    }
}

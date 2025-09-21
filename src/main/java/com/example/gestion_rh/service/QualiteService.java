package com.example.gestion_rh.service;

import com.example.gestion_rh.model.Qualite;
import com.example.gestion_rh.repository.QualiteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QualiteService {
    private final QualiteRepository repo;

    public QualiteService(QualiteRepository repo) {
        this.repo = repo;
    }

    public List<Qualite> getAll() { return repo.findAll(); }

    public Qualite getById(Integer id) { return repo.findById(id).orElse(null); }
}

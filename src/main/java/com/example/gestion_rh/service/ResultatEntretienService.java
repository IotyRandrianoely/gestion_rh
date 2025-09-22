package com.example.gestion_rh.service;


import com.example.gestion_rh.model.ResultatEntretien;
import com.example.gestion_rh.repository.ResultatEntretienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultatEntretienService {

    @Autowired
    private ResultatEntretienRepository repository;

    public List<ResultatEntretien> findAll() {
        return repository.findAll();
    }

    public Optional<ResultatEntretien> findById(Long id) {
        return repository.findById(id);
    }

    public ResultatEntretien save(ResultatEntretien resultatEntretien) {
        return repository.save(resultatEntretien);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
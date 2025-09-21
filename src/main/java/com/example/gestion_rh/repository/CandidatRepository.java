package com.example.gestion_rh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestion_rh.model.Candidat;

public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
    List<Candidat> findByAnnonceId(Integer annonceId);
}

package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.ResultatEntretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultatEntretienRepository extends JpaRepository<ResultatEntretien, Long> {
}
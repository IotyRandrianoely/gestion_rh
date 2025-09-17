package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.Poste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosteRepository extends JpaRepository<Poste, Long> {
}
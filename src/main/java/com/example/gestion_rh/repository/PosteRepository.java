package com.example.gestion_rh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestion_rh.model.Poste;

public interface PosteRepository extends JpaRepository<Poste, Long> {}
package com.example.gestion_rh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestion_rh.model.Annonce;

public interface AnnonceRepository extends JpaRepository<Annonce, Integer> {}
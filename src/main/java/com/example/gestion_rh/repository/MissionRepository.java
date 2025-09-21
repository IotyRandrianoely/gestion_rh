package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Integer> {
}

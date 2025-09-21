package com.example.gestion_rh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestion_rh.model.Mission;
import com.example.gestion_rh.repository.MissionRepository;

@Service
public class MissionService {
    private final MissionRepository repo;

    public MissionService(MissionRepository repo) {
        this.repo = repo;
    }

    public List<Mission> getAll() { return repo.findAll(); }

    public Mission getById(Integer id) { return repo.findById(id).orElse(null); }
}

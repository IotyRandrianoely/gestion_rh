package com.example.gestion_rh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Mission {
    @Id
    private Integer id;
    private String nomMission;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomMission() { return nomMission; }
    public void setNomMission(String nomMission) { this.nomMission = nomMission; }
}

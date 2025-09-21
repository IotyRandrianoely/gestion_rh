package com.example.gestion_rh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Diplome {
    @Id
    private Integer id;
    private String nomDiplome;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomDiplome() { return nomDiplome; }
    public void setNomDiplome(String nomDiplome) { this.nomDiplome = nomDiplome; }
}

package com.example.gestion_rh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Filiere {
    @Id
    private Integer id;
    private String nomFiliere;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomFiliere() { return nomFiliere; }
    public void setNomFiliere(String nomFiliere) { this.nomFiliere = nomFiliere; }
}

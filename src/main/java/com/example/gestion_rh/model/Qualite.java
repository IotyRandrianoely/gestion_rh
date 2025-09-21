package com.example.gestion_rh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Qualite {
    @Id
    private Integer id;
    private String nomQualite;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomQualite() { return nomQualite; }
    public void setNomQualite(String nomQualite) { this.nomQualite = nomQualite; }
}

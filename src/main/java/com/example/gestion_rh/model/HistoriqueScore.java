package com.example.gestion_rh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "historique_score")
public class HistoriqueScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_annonce")
    private Integer annonceId;

    @Column(name = "id_candidat")
    private Long candidatId;

    private Double score;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getAnnonceId() { return annonceId; }
    public void setAnnonceId(Integer annonceId) { this.annonceId = annonceId; }
    public Long getCandidatId() { return candidatId; }
    public void setCandidatId(Long candidatId) { this.candidatId = candidatId; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
}
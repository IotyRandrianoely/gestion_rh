package com.example.gestion_rh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "historique_score")
public class HistoriqueScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "id_annonce")
    private Integer idAnnonce;
    
    @Column(name = "id_candidat")
    private Integer idCandidat;
    
    @Column(name = "score")
    private Double score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(Integer idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public Integer getIdCandidat() {
        return idCandidat;
    }

    public void setIdCandidat(Integer idCandidat) {
        this.idCandidat = idCandidat;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
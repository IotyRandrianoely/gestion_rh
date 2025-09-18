// create table planing_entretien (
//     id INT primary key auto_increment,
//     id_candidat INT,
//     date_debut DATETIME,
//     date_fin DATETIME
// );
package com.example.gestion_rh.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "planing_entretien")
public class PlaningEntretien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_candidat", nullable = false)
    private Candidat candidat;


    @Column(name = "date_debut", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateDebut;
    @Column(name = "date_fin", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateFin;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    

}

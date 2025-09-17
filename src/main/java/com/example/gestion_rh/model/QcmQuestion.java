package com.example.gestion_rh.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "qcm_questions")
public class QcmQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "entity_id", nullable = false)
    private Filiere entity;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "question")
    private List<QcmOption> options;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Filiere getEntity() {
        return entity;
    }

    public void setEntity(Filiere entity) {
        this.entity = entity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<QcmOption> getOptions() {
        return options;
    }

    public void setOptions(List<QcmOption> options) {
        this.options = options;
    }
}
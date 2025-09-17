package com.example.gestion_rh.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "qcm_options")
public class QcmOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QcmQuestion question;

    @Column(name = "option_text", nullable = false, length = 255)
    private String text;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QcmQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QcmQuestion question) {
        this.question = question;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
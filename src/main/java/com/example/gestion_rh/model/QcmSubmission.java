package com.example.gestion_rh.model;

import java.util.List;

public class QcmSubmission {
    private List<UserAnswer> answers;

    public List<UserAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<UserAnswer> answers) {
        this.answers = answers;
    }
}
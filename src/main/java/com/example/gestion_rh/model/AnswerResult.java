package com.example.gestion_rh.model;

import java.util.List;

public class AnswerResult {
    private QcmQuestion question;
    private List<QcmOption> selectedOptions;
    private List<QcmOption> correctOptions;
    private Long timeSpent;

    public QcmQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QcmQuestion question) {
        this.question = question;
    }

    public List<QcmOption> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<QcmOption> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public List<QcmOption> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(List<QcmOption> correctOptions) {
        this.correctOptions = correctOptions;
    }

    public Long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Long timeSpent) {
        this.timeSpent = timeSpent;
    }
}
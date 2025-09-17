package com.example.gestion_rh.service;

import com.example.gestion_rh.model.QcmQuestion;
import com.example.gestion_rh.repository.QcmQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QcmQuestionService {
    @Autowired
    private QcmQuestionRepository questionRepository;

    public List<QcmQuestion> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<QcmQuestion> getQuestionById(Integer id) {
        return questionRepository.findById(id);
    }

    public List<QcmQuestion> getQuestionsByEntityId(Integer entityId) {
        return questionRepository.findByEntityId(entityId);
    }

    public QcmQuestion saveQuestion(QcmQuestion question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }
}
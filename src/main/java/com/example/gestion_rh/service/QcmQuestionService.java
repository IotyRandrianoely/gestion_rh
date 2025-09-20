package com.example.gestion_rh.service;

import com.example.gestion_rh.model.QcmQuestion;
import com.example.gestion_rh.repository.QcmQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.*;

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
    public List<QcmQuestion> getQuestionEntretien(int filiere) {
        // Questions RH (id = 1)
        List<QcmQuestion> questionsRH = questionRepository.findByEntityId(1);
        // Questions de la filière spécifique
        List<QcmQuestion> questionsFiliere = questionRepository.findByEntityId(filiere);
        
        // Mélanger les deux listes
        Collections.shuffle(questionsRH);
        Collections.shuffle(questionsFiliere);
        
        // Créer la liste finale de 20 questions
        List<QcmQuestion> questionsFinales = new ArrayList<>();
        
        // Ajouter 7 questions RH
        int nbQuestionsRH = Math.min(7, questionsRH.size());
        questionsFinales.addAll(questionsRH.subList(0, nbQuestionsRH));
        
        // Ajouter les questions de la filière (13 ou plus si pas assez de questions RH)
        int nbQuestionsFiliere = Math.min(20 - nbQuestionsRH, questionsFiliere.size());
        questionsFinales.addAll(questionsFiliere.subList(0, nbQuestionsFiliere));
        
        // Mélanger une dernière fois pour que les questions RH ne soient pas toutes au début
        Collections.shuffle(questionsFinales);
        
        return questionsFinales;
    }
}
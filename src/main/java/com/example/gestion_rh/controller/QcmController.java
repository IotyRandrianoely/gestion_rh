package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.gestion_rh.service.QcmOptionService;
import com.example.gestion_rh.service.QcmQuestionService;
import com.example.gestion_rh.model.*;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/qcm")
public class QcmController {

    @Autowired
    private QcmOptionService optionService;
    
    @Autowired
    private QcmQuestionService questionService;

    @GetMapping("/questions")
    public String listQuestions(Model model) {
        model.addAttribute("questions", questionService.getAllQuestions());
        return "qcm/questions";
    }

    @GetMapping("/entretien/{filiereId}")
    public String getQuestionsEntretien(@PathVariable int filiereId,  Model model) {
        List<QcmQuestion> allQuestions = questionService.getQuestionEntretien(filiereId);
        List<QcmQuestion> selectedQuestions = new ArrayList<>();
        
        // Ajouter un objet vide pour la soumission du formulaire
        model.addAttribute("qcmSubmission", new QcmSubmission());
        
        // Si c'est l'ID 1, on prend 10 questions
        if (filiereId == 1) {
            int count = 0;
            for (QcmQuestion question : allQuestions) {
                if (count >= 10) break;
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                question.setOptions(options);
                selectedQuestions.add(question);
                count++;
            }
        } else {
            // Pour les autres IDs, on prend 3 questions de l'ID 1 et le reste d'autres filières
            List<QcmQuestion> filiereOneQuestions = questionService.getQuestionEntretien(1);
            
            // Prendre 3 questions de l'ID 1
            for (int i = 0; i < 3 && i < filiereOneQuestions.size(); i++) {
                QcmQuestion question = filiereOneQuestions.get(i);
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                question.setOptions(options);
                selectedQuestions.add(question);
            }
            
            // Prendre le reste des questions de la filière actuelle
            int remainingCount = 10 - selectedQuestions.size();
            for (QcmQuestion question : allQuestions) {
                if (selectedQuestions.size() >= 10) break;
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                question.setOptions(options);
                selectedQuestions.add(question);
            }
        }
        
        model.addAttribute("questions", selectedQuestions);
        model.addAttribute("filiereId", filiereId);
        return "qcm/entretien";
    }

    @GetMapping("/question/{id}/correct-options")
    public String showCorrectOptions(@PathVariable Integer id, Model model) {
        QcmQuestion question = questionService.getQuestionById(id).orElse(null);
        List<QcmOption> correctOptions = optionService.getCorrectOptionsByQuestionId(id);
        
        model.addAttribute("question", question);
        model.addAttribute("correctOptions", correctOptions);
        return "qcm/correct-options";
    }

    @PostMapping("/submit-answers")
    public String submitAnswers(@ModelAttribute("qcmSubmission") QcmSubmission submission, Model model) {
        List<UserAnswer> answers = submission != null ? submission.getAnswers() : new ArrayList<>();
        if (answers == null) {
            answers = new ArrayList<>();
        }
        
        QcmResult result = new QcmResult();
        result.setUserAnswers(answers);
        result.setTotalQuestions(10); // Nombre total fixé à 10
        
        List<AnswerResult> detailedResults = new ArrayList<>();
        int score = 0;
        
        for (UserAnswer answer : answers) {
            QcmQuestion question = questionService.getQuestionById(answer.getQuestionId()).orElse(null);
            List<QcmOption> correctOptions = optionService.getCorrectOptionsByQuestionId(answer.getQuestionId());
            List<Integer> selectedIds = answer.getSelectedOptionIds();
            List<QcmOption> selectedOptions = new ArrayList<>();
            
            // Récupérer les options sélectionnées
            for (Integer optionId : selectedIds) {
                optionService.getOptionById(optionId).ifPresent(selectedOptions::add);
            }
            
            // Créer le résultat détaillé pour cette question
            AnswerResult answerResult = new AnswerResult();
            answerResult.setQuestion(question);
            answerResult.setSelectedOptions(selectedOptions);
            answerResult.setCorrectOptions(correctOptions);
            answerResult.setTimeSpent(answer.getTimeSpent());
            detailedResults.add(answerResult);
            
            // Calcul du score
            boolean allCorrect = true;
            if (selectedIds != null && correctOptions != null) {
                // Vérifie si toutes les options sélectionnées sont correctes
                for (Integer selectedId : selectedIds) {
                    boolean isCorrect = false;
                    for (QcmOption correctOption : correctOptions) {
                        if (selectedId.equals(correctOption.getId())) {
                            isCorrect = true;
                            break;
                        }
                    }
                    if (!isCorrect) {
                        allCorrect = false;
                        break;
                    }
                }
                
                // Vérifie si toutes les réponses correctes ont été sélectionnées
                if (allCorrect && selectedIds.size() == correctOptions.size()) {
                    score++;
                }
            }
        }
        
        result.setScore(score);
        result.setPercentage((double) score / Math.max(1, answers.size()) * 100);
        
        model.addAttribute("result", result);
        model.addAttribute("answers", detailedResults);
        return "qcm/results";
    }
}
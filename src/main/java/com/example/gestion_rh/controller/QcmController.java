package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.gestion_rh.service.QcmOptionService;
import com.example.gestion_rh.service.QcmQuestionService;
import com.example.gestion_rh.service.HistoriqueScoreService;
import com.example.gestion_rh.service.AnnonceService;
import com.example.gestion_rh.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Controller
@RequestMapping("/qcm")
public class QcmController {

    @Autowired
    private QcmOptionService optionService;
    
    @Autowired
    private QcmQuestionService questionService;
    
    @Autowired
    private HistoriqueScoreService historiqueScoreService;
    
    @Autowired
    private AnnonceService annonceService;

    @GetMapping("/questions")
    public String listQuestions(Model model) {
        model.addAttribute("questions", questionService.getAllQuestions());
        return "qcm/questions";
    }

    @GetMapping("/start")
    public String startQcm(
            @RequestParam(defaultValue = "1") Integer candidatId,
            @RequestParam(defaultValue = "1") Integer annonceId,
            Model model) {
        
        // Récupérer l'ID de la filière à partir de l'annonce
        Integer filiereId = annonceService.getFiliereIdByAnnonceId(annonceId);
        if (filiereId == null) {
            // Si pas de filière trouvée, rediriger vers une page d'erreur ou utiliser une valeur par défaut
            filiereId = 1;
        }
        
        model.addAttribute("candidatId", candidatId);
        model.addAttribute("annonceId", annonceId);
        return "qcm/start";
    }

    @PostMapping("/start-test")
    public String startTest(
            @RequestParam Integer candidatId,
            @RequestParam Integer annonceId,
            Model model) {
        
        // Récupérer l'ID de la filière à partir de l'annonce
        Integer filiereId = annonceService.getFiliereIdByAnnonceId(annonceId);
        if (filiereId == null) {
            // Rediriger vers une page d'erreur si nécessaire
            return "redirect:/error";
        }
        
        return "redirect:/qcm/entretien/" + filiereId + "?candidatId=" + candidatId + "&annonceId=" + annonceId;
    }

    @GetMapping("/entretien/{filiereId}")
    public String getQuestionsEntretien(
            @PathVariable int filiereId,
            @RequestParam(defaultValue = "7") Integer candidatId,
            @RequestParam(defaultValue = "1") Integer annonceId,
            Model model) {
        
        List<QcmQuestion> selectedQuestions = new ArrayList<>();
        
        if (filiereId == 1) {
            // Pour ID 1, prendre toutes les questions de la filière 1
            List<QcmQuestion> filiereQuestions = questionService.getQuestionEntretien(filiereId);
            for (QcmQuestion question : filiereQuestions) {
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                if (options != null && !options.isEmpty()) {  // Vérifier si la question a des options
                    question.setOptions(options);
                    selectedQuestions.add(question);
                    if (selectedQuestions.size() >= 10) break; // Limiter à 10 questions
                }
            }
        } else {
            // Pour les autres filières : 70% filière spécifique, 30% RH (ID 1)
            List<QcmQuestion> filiereQuestions = questionService.getQuestionEntretien(filiereId);
            List<QcmQuestion> rhQuestions = questionService.getQuestionEntretien(1);
            
            // Calculer le nombre de questions pour chaque catégorie
            int totalQuestions = 10;
            int rhQuestionsCount = (int) Math.ceil(totalQuestions * 0.3); // 30% questions RH
            int filiereQuestionsCount = totalQuestions - rhQuestionsCount; // 70% questions filière
            
            // Ajouter les questions RH (30%)
            for (QcmQuestion question : rhQuestions) {
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                if (options != null && !options.isEmpty()) {
                    question.setOptions(options);
                    selectedQuestions.add(question);
                    if (selectedQuestions.size() >= rhQuestionsCount) break;
                }
            }
            
            // Ajouter les questions de la filière spécifique (70%)
            for (QcmQuestion question : filiereQuestions) {
                List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                if (options != null && !options.isEmpty()) {
                    question.setOptions(options);
                    selectedQuestions.add(question);
                    if (selectedQuestions.size() >= totalQuestions) break;
                }
            }
        }
        
        // Si on n'a pas assez de questions valides, compléter avec des questions RH
        if (selectedQuestions.size() < 10) {
            List<QcmQuestion> rhQuestions = questionService.getQuestionEntretien(1);
            for (QcmQuestion question : rhQuestions) {
                if (!selectedQuestions.contains(question)) {
                    List<QcmOption> options = optionService.getOptionsByQuestionId(question.getId());
                    if (options != null && !options.isEmpty()) {
                        question.setOptions(options);
                        selectedQuestions.add(question);
                        if (selectedQuestions.size() >= 10) break;
                    }
                }
            }
        }
        
        // Mélanger les questions pour plus d'aléatoire
        Collections.shuffle(selectedQuestions);
        
        // Ajouter les attributs au modèle
        model.addAttribute("candidatId", candidatId);
        model.addAttribute("annonceId", annonceId);
        model.addAttribute("filiereId", filiereId);
        model.addAttribute("qcmSubmission", new QcmSubmission());
        model.addAttribute("questions", selectedQuestions);
        
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
    public String submitAnswers(
            @ModelAttribute("qcmSubmission") QcmSubmission submission,
            @RequestParam(defaultValue = "7") Integer candidatId,
            @RequestParam(defaultValue = "1") Integer annonceId,
            Model model) {
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
        
        // Sauvegarder le score dans l'historique
        HistoriqueScore historiqueScore = new HistoriqueScore();
        historiqueScore.setIdCandidat(candidatId);
        historiqueScore.setIdAnnonce(annonceId);
        historiqueScore.setScore((double) result.getScore());
        historiqueScoreService.saveScore(historiqueScore);

        model.addAttribute("result", result);
        model.addAttribute("answers", detailedResults);
        return "qcm/results";
    }
}
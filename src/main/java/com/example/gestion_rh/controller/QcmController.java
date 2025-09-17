package com.example.gestion_rh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.gestion_rh.service.QcmOptionService;
import com.example.gestion_rh.service.QcmQuestionService;
import com.example.gestion_rh.model.QcmQuestion;
import com.example.gestion_rh.model.QcmOption;
import java.util.List;

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
    public String getQuestionsEntretien(@PathVariable int filiereId, Model model) {
        List<QcmQuestion> questionsEntretien = questionService.getQuestionEntretien(filiereId);
        model.addAttribute("questions", questionsEntretien);
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
}
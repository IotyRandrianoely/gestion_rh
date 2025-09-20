package com.example.gestion_rh.service;

import com.example.gestion_rh.model.QcmOption;
import com.example.gestion_rh.repository.QcmOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QcmOptionService {
    @Autowired
    private QcmOptionRepository optionRepository;

    public List<QcmOption> getAllOptions() {
        return optionRepository.findAll();
    }

    
    public Optional<QcmOption> getOptionById(Integer id) {
        return optionRepository.findById(id);
    }

    public List<QcmOption> getOptionsByQuestionId(Integer questionId) {
        return optionRepository.findByQuestionId(questionId);
    }
    public List<QcmOption> getCorrectOptionsByQuestionId(Integer questionId) {
        return optionRepository.findCorrectOptionsByQuestionId(questionId);
    }
    public QcmOption saveOption(QcmOption option) {
        return optionRepository.save(option);
    }

    public void deleteOption(Integer id) {
        optionRepository.deleteById(id);
    }
}
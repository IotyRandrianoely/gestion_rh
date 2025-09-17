package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.QcmQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QcmQuestionRepository extends JpaRepository<QcmQuestion, Integer> {
    List<QcmQuestion> findByEntityId(Integer entityId);
}

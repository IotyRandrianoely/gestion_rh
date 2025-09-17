package com.example.gestion_rh.repository;

import com.example.gestion_rh.model.QcmOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QcmOptionRepository extends JpaRepository<QcmOption, Integer> {
    List<QcmOption> findByQuestionId(Integer questionId);
    
    @Query(value = "SELECT * FROM qcm_options WHERE is_correct = true AND question_id = :questionId", nativeQuery = true)
    List<QcmOption> findCorrectOptionsByQuestionId(@Param("questionId") Integer questionId);


}
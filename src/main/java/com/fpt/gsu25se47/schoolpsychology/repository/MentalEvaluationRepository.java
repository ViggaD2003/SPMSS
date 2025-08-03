package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MentalEvaluationRepository extends JpaRepository<MentalEvaluation, Integer> {

//    @Query("""
//            SELECT m FROM MentalEvaluation m
//            WHERE m.student.id = :studentId
//            AND (:from IS NULL OR m.date >= :from)
//            AND (:to IS NULL OR m.date <= :to)
//            AND (:evaluationType IS NULL OR m.evaluationType = :evaluationType)
//            """)
//    Page<MentalEvaluation> findAllByStudentIdAndDateBetweenAndEvaluationType(int studentId, LocalDate from, LocalDate to, EvaluationType evaluationType, Pageable pageable);
//
//    @Query("""
//            SELECT m FROM MentalEvaluation m
//            WHERE (:from IS NULL OR m.date >= :from)
//            AND (:to IS NULL OR m.date <= :to)
//            AND (:evaluationType IS NULL OR m.evaluationType = :evaluationType)
//            """)
//    Page<MentalEvaluation> findAllByDateBetweenAndEvaluationType(LocalDate from, LocalDate to, EvaluationType evaluationType, Pageable pageable);
}

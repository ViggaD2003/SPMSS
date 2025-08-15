package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    @Query("""
    SELECT m 
    FROM MentalEvaluation m
    WHERE (
            (:type = 'survey' AND m.surveyRecord IS NOT NULL) OR
            (:type = 'appointment' AND m.appointment IS NOT NULL) OR
            (:type = 'program' AND m.programParticipants IS NOT NULL)
          )
      AND (
        (:from IS NULL AND :to IS NULL) 
        OR (:from IS NOT NULL AND :to IS NOT NULL AND m.latestEvaluatedAt BETWEEN :from AND :to)
        OR (:from IS NOT NULL AND :to IS NULL AND m.latestEvaluatedAt >= :from)
        OR (:from IS NULL AND :to IS NOT NULL AND m.latestEvaluatedAt <= :to)
      )
      AND (:studentId IS NULL OR m.student.id = :studentId)
""")
    List<MentalEvaluation> findEvaluationsByTypeAndDate(
            @Param("type") String type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("studentId") Integer studentId
    );



    @Query("SELECT m FROM MentalEvaluation m WHERE m.programParticipants.id = :id")
    MentalEvaluation findMentalEvaluationByProgramParticipantsId(Integer id);
}

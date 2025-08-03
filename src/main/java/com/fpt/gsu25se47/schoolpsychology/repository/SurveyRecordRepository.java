package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyRecordRepository extends JpaRepository<SurveyRecord, Integer> {

    @Query("SELECT sr FROM SurveyRecord sr " +
            "WHERE sr.student.id = :studentId " +
            "AND (:surveyType IS NULL OR sr.survey.surveyType = :surveyType)")
    Page<SurveyRecord> findAllSurveyRecordsByStudentIdAndSurveyType(@Param("studentId") int studentId,
                                                                    @Param("surveyType") SurveyType surveyType,
                                                                    Pageable pageable);

    @Query("SELECT COUNT(sr) FROM SurveyRecord sr " +
            "WHERE sr.student.id = :studentId AND sr.isSkipped = true")
    int countSkippedSurveyRecordsByStudentId(@Param("studentId") int studentId);

    @Query("SELECT sr FROM SurveyRecord sr WHERE sr.survey.id =:surveyId")
    List<SurveyRecord> findAllBySurveyId(Integer surveyId);

    @Query(value = """
    SELECT sr.* 
    FROM program_participants pp
    JOIN support_program sp ON pp.program_id = sp.id
    JOIN survey s ON sp.survey_id = s.id
    JOIN survey_record sr ON sr.survey_id = s.id AND sr.account_id = pp.student_id
    WHERE pp.id = :participantId
    ORDER BY sr.completed_at
    LIMIT 2
    """, nativeQuery = true)
    List<SurveyRecord> findTwoSurveyRecordsByParticipant(@Param("participantId") Integer participantId);

}

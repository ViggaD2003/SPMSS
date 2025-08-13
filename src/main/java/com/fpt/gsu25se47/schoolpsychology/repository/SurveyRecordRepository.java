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

    @Query("SELECT sr FROM SurveyRecord sr WHERE sr.survey.id = :surveyId AND sr.student.id = :studentId")
    List<SurveyRecord> findAllByStudentIdAndSurveyId(Integer studentId, Integer surveyId);

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

    @Query("""
                SELECT sr FROM SurveyRecord sr
                WHERE sr.student.id = :studentId
                  AND sr.isSkipped = true
                  AND FUNCTION('MONTH', sr.completedAt) = FUNCTION('MONTH', CURRENT_DATE)
                  AND FUNCTION('YEAR', sr.completedAt) = FUNCTION('YEAR', CURRENT_DATE)
            """)
    List<SurveyRecord> findSkippedSurveyRecordsByStudentIdInCurrentMonth(@Param("studentId") int studentId);

    @Query(value = """
            SELECT *
            FROM survey_record
            WHERE account_id = :studentId
            ORDER BY completed_at DESC
            LIMIT 1;
            """, nativeQuery = true)
    SurveyRecord findLatestSurveyRecordByStudentId(Integer studentId);

    @Query(value = """
        SELECT EXISTS(
            SELECT 1
            FROM survey_record sr
            INNER JOIN program_participants pp ON sr.account_id = pp.student_id
            WHERE sr.survey_record_type = 'PROGRAM'
                AND sr.survey_record_identify = 'ENTRY'
                AND pp.student_id = :studentId
                AND pp.program_id = :supportProgramId
        )
        """, nativeQuery = true)
    boolean isEntrySurveyRecordByStudentId(@Param("studentId") Integer studentId,
                                                @Param("supportProgramId") Integer supportProgramId);
    @Query(value = """
        SELECT EXISTS(
            SELECT 1
            FROM survey_record sr
            INNER JOIN program_participants pp ON sr.account_id = pp.student_id
            WHERE sr.survey_record_type = 'PROGRAM'
                AND sr.survey_record_identify = 'EXIT'
                AND pp.student_id = :studentId
                AND pp.program_id = :supportProgramId
        )
        """, nativeQuery = true)
    boolean isExistSurveyRecordByStudentId(@Param("studentId") Integer studentId,
                                                @Param("supportProgramId") Integer supportProgramId);
}

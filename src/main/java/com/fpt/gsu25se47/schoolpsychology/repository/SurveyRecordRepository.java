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

    @Query("""
                SELECT sr FROM SurveyRecord sr
                WHERE sr.student.id = :studentId
                  AND (
                        (:surveyType IS NULL AND sr.survey.surveyType IN ('SCREENING', 'FOLLOWUP'))
                        OR (:surveyType IS NOT NULL AND sr.survey.surveyType = :surveyType)
                  )
            """)
    Page<SurveyRecord> findAllSurveyRecordsByStudentIdAndSurveyType(
            @Param("studentId") int studentId,
            @Param("surveyType") SurveyType surveyType,
            Pageable pageable
    );

    @Query("SELECT COUNT(sr) FROM SurveyRecord sr " +
            "WHERE sr.student.id = :studentId AND sr.isSkipped = true")
    int countSkippedSurveyRecordsByStudentId(@Param("studentId") int studentId);

    @Query("SELECT sr FROM SurveyRecord sr WHERE sr.survey.id =:surveyId")
    List<SurveyRecord> findAllBySurveyId(Integer surveyId);

    @Query("SELECT sr FROM SurveyRecord sr WHERE sr.survey.id = :surveyId AND sr.student.id = :studentId")
    List<SurveyRecord> findAllByStudentIdAndSurveyId(Integer studentId, Integer surveyId);

    @Query("""
            SELECT sr
            FROM ProgramParticipants pp
            JOIN pp.program sp
            JOIN sp.survey s
            JOIN SurveyRecord sr ON sr.survey = s AND sr.student.id = pp.student.id
            WHERE pp.id = :participantId
            ORDER BY sr.completedAt
            """)
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

    @Query("""
                SELECT CASE WHEN COUNT(sr) > 0 THEN true ELSE false END
                FROM SurveyRecord sr
                JOIN ProgramParticipants pp ON sr.student.id = pp.student.id
                WHERE sr.surveyRecordType = 'PROGRAM'
                  AND sr.surveyRecordIdentify = 'ENTRY'
                  AND sr.student.id = :studentId
                  AND pp.program.id = :supportProgramId
            """)
    Boolean isEntrySurveyRecordByStudentId(@Param("studentId") Integer studentId,
                                           @Param("supportProgramId") Integer supportProgramId);


    @Query("""
                SELECT CASE WHEN COUNT(sr) > 0 THEN true ELSE false END
                FROM SurveyRecord sr
                JOIN ProgramParticipants pp ON sr.student.id = pp.student.id
                WHERE sr.surveyRecordType = 'PROGRAM'
                  AND sr.surveyRecordIdentify = 'EXIT'
                  AND sr.student.id = :studentId
                  AND pp.program.id = :supportProgramId
            """)
    Boolean isExitSurveyRecordByStudentId(@Param("studentId") Integer studentId,
                                          @Param("supportProgramId") Integer supportProgramId);
}

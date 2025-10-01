package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    @Query("SELECT s FROM Survey s WHERE s.createBy.id = :accountId")
    List<Survey> findByAccountId(int accountId);

    @Query("SELECT s FROM Survey s WHERE s.startDate = :date AND s.status = 'DRAFT'")
    List<Survey> findByStartDateAndStatusDraft(LocalDate date);

    @Query("SELECT s FROM Survey s WHERE s.endDate = :date AND s.status = 'PUBLISHED'")
    List<Survey> findByEndDateAndStatusPublished(LocalDate date);

    @Query(value = """
            SELECT DISTINCT s.*
            FROM survey s
            JOIN students st ON st.id = :accountId
            WHERE s.status = 'PUBLISHED'
              AND st.is_enable_survey = TRUE
              AND (
                   s.target_grade_level IS NULL
                OR JSON_LENGTH(COALESCE(NULLIF(s.target_grade_level,''),'[]')) = 0
                OR JSON_CONTAINS(s.target_grade_level, JSON_QUOTE(st.target_level))
              )
              AND (
                -- Case 1: KHÔNG có case đang mở -> chỉ cho SCREENING chưa làm ở round hiện tại
                (
                  NOT EXISTS (
                    SELECT 1 FROM cases c
                    WHERE c.student_id = st.id
                      AND c.status <> 'CLOSED'
                  )
                  AND s.survey_type = 'SCREENING'
                  AND NOT EXISTS (
                    SELECT 1 FROM survey_record sr
                    WHERE sr.survey_id   = s.id
                      AND sr.account_id  = st.id
                      AND sr.round       = s.round
                      AND (sr.survey_record_type = 'SCREENING' OR sr.survey_record_type IS NULL)
                  )
                )
                OR
                -- Case 2: CÓ case đang mở -> chỉ cho FOLLOWUP đã được assign,
                -- và CHƯA có survey_record hoàn thành SAU khi case được tạo
                (
                  s.survey_type = 'FOLLOWUP'
                  AND EXISTS (
                    SELECT 1
                    FROM survey_case_link scl
                    JOIN cases c2 ON c2.id = scl.case_id
                    WHERE scl.survey_id = s.id
                      AND c2.student_id = st.id
                      AND c2.status <> 'CLOSED'
                      AND (scl.is_active IS NULL OR scl.is_active = TRUE)
                      AND NOT EXISTS (
                        SELECT 1
                        FROM survey_record sr2
                        WHERE sr2.survey_id          = s.id
                          AND sr2.account_id         = st.id
                          AND sr2.round              = s.round
                          AND sr2.survey_record_type = 'FOLLOWUP'
                          AND sr2.completed_at IS NOT NULL
                          AND sr2.completed_at >= c2.created_date
                      )
                  )
                )
              )
            ORDER BY s.created_date DESC
            """, nativeQuery = true)
    List<Survey> findUnansweredExpiredSurveysByAccountId(@Param("accountId") Integer accountId);

    @Query(value = """
                SELECT DISTINCT s.*
                FROM survey s
                         JOIN survey_case_link scl ON s.id = scl.survey_id
                         JOIN cases c ON scl.case_id = c.id
                WHERE scl.is_active = 1 AND c.status = 'IN_PROGRESS'
                ORDER BY s.created_date DESC;
            """, nativeQuery = true)
    List<Survey> findAllSurveyIsActiveInCase();

    @Query("""
            SELECT s FROM Survey s
            JOIN SurveyCaseLink scl ON s.id = scl.survey.id
            JOIN Cases c ON scl.cases.id = c.id
            WHERE c.id = :caseId
            """)
    List<Survey> findAllSurveyByCaseId(Integer caseId);

    @Query("""
            SELECT s FROM Survey s
            JOIN SurveyCaseLink scl ON s.id = scl.survey.id
            JOIN Cases c ON scl.cases.id = c.id
            WHERE c.id = :caseId and scl.isActive = true
            """)
    List<Survey> findAllSurveyWithLinkActiveByCaseId(Integer caseId);

    @Query("SELECT s FROM Survey s WHERE s.isRecurring = true AND s.status = 'ARCHIVED'")
    List<Survey> findAllRecurringSurveys();

    @Query("""
                SELECT COUNT(s) 
                FROM Survey s
                LEFT JOIN SurveyRecord sr ON s.id = sr.survey.id
                WHERE sr.isSkipped = true 
                  AND sr.student.id = :studentId
                  AND sr.completedAt BETWEEN :startDate AND :endDate
            """)
    int countSurveySkip(Integer studentId, LocalDateTime startDate, LocalDateTime endDate);


    @Query("""
                SELECT s FROM Survey s
                WHERE
                    s.startDate < :startDate AND
                    s.endDate > :endDate AND
                    s.status = 'PUBLISHED'
            """)
    List<Survey> findSurveysBetween(LocalDate startDate, LocalDate endDate);

}

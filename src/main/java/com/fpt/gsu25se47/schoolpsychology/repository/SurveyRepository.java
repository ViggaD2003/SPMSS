package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    @Query("SELECT s FROM Survey s WHERE s.createBy.id = :accountId")
    List<Survey> findByAccountId(int accountId);

    @Query("SELECT s FROM Survey s WHERE s.startDate >= :date AND s.status = 'DRAFT'")
    List<Survey> findByStartDateAndStatusDraft(LocalDate date);

    @Query("SELECT s FROM Survey s WHERE s.endDate <= :date AND s.status = 'PUBLISHED'")
    List<Survey> findByEndDateAndStatusPublished(LocalDate date);

    @Query(value = """
                SELECT DISTINCT s.*
                FROM survey s
                         JOIN students st ON st.id = :accountId
                WHERE s.status = 'PUBLISHED'
                  AND st.is_enable_survey = true
                  AND (
                    -- Xử lý target_grade_level
                        s.target_grade_level = '[]'
                        OR JSON_LENGTH(s.target_grade_level) = 0
                        OR s.target_grade_level LIKE CONCAT('%', st.target_level, '%')
                    )
                  AND (
                    -- SCREENING: hiển thị cho tất cả students
                    s.survey_type = 'SCREENING'
                        OR
                        -- FOLLOWUP: chỉ hiển thị nếu student có case và survey được assign
                    (s.survey_type = 'FOLLOWUP'
                        AND EXISTS (
                            SELECT 1
                            FROM survey_case_link scl
                                     JOIN cases c ON scl.case_id = c.id
                            WHERE scl.survey_id = s.id
                              AND c.student_id = st.id
                              AND (scl.is_active IS NULL OR scl.is_active = true)
                        ))
                    )
                  AND NOT EXISTS (
                    SELECT 1
                    FROM survey_record sr
                    WHERE sr.survey_id = s.id
                      AND sr.account_id = st.id
                      AND sr.round = s.round
                )
                ORDER BY s.created_date DESC;
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

    @Query("SELECT s FROM Survey s WHERE s.isRecurring = true AND s.status = 'ARCHIVED'")
    List<Survey> findAllRecurringSurveys();

}

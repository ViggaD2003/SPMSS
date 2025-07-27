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
                SELECT DISTINCT s.*\s
                FROM survey s
                JOIN students st ON st.id = :accountId
                WHERE s.status = 'PUBLISHED'
                  AND st.is_enable_survey = true
                  AND (
                    JSON_LENGTH(s.target_grade_level) = 0
                    OR s.target_grade_level LIKE CONCAT('%', st.target_level, '%')
                  )
                  AND NOT EXISTS (
                    SELECT 1\s
                    FROM survey_record sr\s
                    WHERE sr.survey_id = s.id\s
                      AND sr.account_id = st.id
                  )
                ORDER BY s.created_date DESC;
            """, nativeQuery = true)
    List<Survey> findUnansweredExpiredSurveysByAccountId(@Param("accountId") Integer accountId);

    @Query(value = """
                SELECT DISTINCT s.*
                FROM survey s
                         JOIN survey_case_link scl ON s.id = scl.survey_id
                         JOIN cases c ON scl.case_id = c.id
                WHERE c.student_id = :accountId AND c.status = 'IN_PROGRESS'
                ORDER BY s.created_date DESC;
            """, nativeQuery = true)
    List<Survey> findAllSurveyStudentInCase(Integer accountId);

    @Query("SELECT s FROM Survey s WHERE s.isRecurring = true AND s.status != 'ARCHIVED'")
    List<Survey> findAllRecurringSurveys();

}

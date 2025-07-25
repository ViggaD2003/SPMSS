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

    @Query("SELECT s FROM Survey s WHERE s.startDate = :date AND s.status = 'DRAFT'")
    List<Survey> findByStartDateAndStatusDraft(LocalDate date);

    @Query("SELECT s FROM Survey s WHERE s.endDate <= :date AND s.status = 'PUBLISHED'")
    List<Survey> findByEndDateAndStatusPublished(LocalDate date);

    @Query(value = """
                SELECT s.* FROM survey s
                JOIN students st ON st.id = :accountId
                WHERE s.status = 'PUBLISHED'
                  AND st.is_enable_survey = true
                  AND (
                        JSON_LENGTH(s.target_grade_level) = 0
                        OR s.target_grade_level LIKE CONCAT('%', st.target_level, '%')
                  )
            """, nativeQuery = true)
    List<Survey> findUnansweredExpiredSurveysByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT s FROM Survey s WHERE s.isRecurring = true AND s.status != 'ARCHIVED'")
    List<Survey> findAllRecurringSurveys();

}

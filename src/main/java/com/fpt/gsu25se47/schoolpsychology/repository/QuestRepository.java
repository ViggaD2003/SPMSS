package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestRepository extends JpaRepository<Question, Integer> {
//    @Query("SELECT q FROM Question q WHERE q.programSurvey.id = :surveyId AND q.isRequired = true")
//    List<Question> findByProgramSurveyIdAndIsRequiredTrue(@Param("surveyId") Integer surveyId);

}

package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SurveyCaseLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyCaseLinkRepository extends JpaRepository<SurveyCaseLink, Integer> {

    @Query("SELECT scl FROM SurveyCaseLink scl WHERE scl.survey.id = :surveyId")
    List<SurveyCaseLink> findAllBySurveyId(Integer surveyId);

    @Query("SELECT scl FROM SurveyCaseLink scl WHERE scl.survey.id =:surveyId AND scl.cases.id =:caseId")
    SurveyCaseLink existsBySurveyIdAndCaseId(Integer surveyId, Integer caseId);
}

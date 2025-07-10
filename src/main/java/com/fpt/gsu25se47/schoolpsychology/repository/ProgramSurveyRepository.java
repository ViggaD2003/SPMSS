package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramSurveyRepository extends JpaRepository<ProgramSurvey, Integer> {

}

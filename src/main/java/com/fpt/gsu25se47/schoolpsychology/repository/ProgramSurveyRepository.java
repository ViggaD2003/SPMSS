package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramSurveyRepository extends JpaRepository<ProgramSurvey, Integer> {

    @Query("SELECT ps FROM ProgramSurvey ps WHERE ps.program.id = :id")
    List<ProgramSurvey> findAllBySupportProgramId(Integer id);
}

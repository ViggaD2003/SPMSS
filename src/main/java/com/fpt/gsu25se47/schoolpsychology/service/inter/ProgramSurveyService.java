package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewProgramSurvey;

import java.util.Optional;

public interface ProgramSurveyService {

    Optional<?> addNewPrgSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSupportId);

    Optional<?> getAllPrgSurvey(Integer supportProgramId);

    Optional<?> updatePrgSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSurveyId);
}

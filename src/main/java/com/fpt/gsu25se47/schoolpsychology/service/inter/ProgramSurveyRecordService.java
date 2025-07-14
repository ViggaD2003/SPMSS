package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramRecordDto;

import java.util.Optional;

public interface ProgramSurveyRecordService {

    Optional<?> submitProgramRecord(CreateProgramRecordDto dto);

    Optional<?> showProgramRecord(Integer programSurveyId, Integer registerId);
}

package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramParticipantsResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface SupportProgramService {
    SupportProgramResponse createSupportProgram(SupportProgramRequest request, HttpServletRequest servletRequest);

    SupportProgramResponse getSupportProgramById(Integer id);

    List<SupportProgramResponse> getAllSupportPrograms();

    List<ProgramParticipantsResponse> addParticipantsToSupportProgram(Integer supportProgramId, List<Integer> caseIds);

    //    SupportProgramResponse updateSupportProgram(Integer id, SupportProgramRequest request);
    Optional<?> saveSurveySupportProgram(CreateSurveyRecordDto createSurveyRecordDto);
}

package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SupportProgramService {
    SupportProgramResponse createSupportProgram(MultipartFile thumbnail, SupportProgramRequest request) throws IOException;

    SupportProgramDetail getSupportProgramById(Integer id);

    List<SupportProgramResponse> getAllSupportPrograms();

    List<ProgramParticipantsResponse> addParticipantsToSupportProgram(Integer supportProgramId, List<Integer> caseIds);

    SupportProgramResponse updateSupportProgram(Integer id, ProgramStatus status);

    Optional<?> saveSurveySupportProgram(Integer programId, CreateSurveyRecordDto createSurveyRecordDto);

    RegisterProgramParticipantResponse registerStudentToSupportProgram(Integer supportProgramId);

    String unRegisterStudentFromSupportProgram(Integer supportProgramId, Integer studentId);

    SupportProgramStudentDetail getSupportProgramStudentDetailById(Integer supportProgramId, Integer studentId);

    Optional<?> getSuggestSupportProgram(Integer studentId);}

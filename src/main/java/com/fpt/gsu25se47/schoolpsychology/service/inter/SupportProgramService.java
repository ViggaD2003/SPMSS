package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSupportProgramRequest;
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

    SupportProgramResponse updateStatusSupportProgram(Integer id, ProgramStatus status);

    SupportProgramResponse updateSupportProgram(Integer id, UpdateSupportProgramRequest request);

    Optional<?> saveSurveySupportProgram(Integer programId, Integer studentId, CreateSurveyRecordDto createSurveyRecordDto);

    RegisterProgramParticipantResponse registerStudentToSupportProgram(Integer supportProgramId);

    String unRegisterStudentFromSupportProgram(Integer supportProgramId, Integer studentId);

    SupportProgramStudentDetail getSupportProgramStudentDetailById(Integer supportProgramId, Integer studentId);

    List<SupportProgramResponse> getSuggestSupportProgram(Integer studentId);

    List<SupportProgramPPResponse> getSupportProgramsByStudentId(Integer studentId);

    String openSurvey(Integer supportProgramId);

    List<SupportProgramResponse> getAllActiveSupportPrograms();

    String addNewThumbnail(Integer programId, MultipartFile thumbnail) throws IOException;

    String deleteThumbnail(Integer programId, String public_id);

}

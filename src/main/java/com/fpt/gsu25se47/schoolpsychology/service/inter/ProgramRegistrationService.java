package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ProgramRegistrationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramRegistrationResponse;

import java.util.List;

public interface ProgramRegistrationService {

    ProgramRegistrationResponse createProgramRegistration(ProgramRegistrationRequest request);
    List<ProgramRegistrationResponse> getAllProgramRegistrationsByProgramId(Integer supportProgramId);
    List<ProgramRegistrationResponse> getAllProgramRegistrationsByAccountId(Integer accountId);
    ProgramRegistrationResponse updateProgramRegistration(Integer programRegistrationId, ProgramRegistrationRequest request);
    ProgramRegistrationResponse getById(Integer programRegistrationId);
}

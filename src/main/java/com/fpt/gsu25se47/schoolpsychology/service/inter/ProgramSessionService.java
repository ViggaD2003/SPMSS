package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;

import java.util.List;

public interface ProgramSessionService {
    ProgramSessionResponse createProgramSession(CreateProgramSessionRequest request);
    List<ProgramSessionResponse> getAllProgramSessionsBySupportProgramId(Integer supportProgramId);
    List<ProgramSessionResponse> getAllProgramSessions();
    ProgramSessionResponse getProgramSessionById(Integer id);
    void deleteProgramSession(Integer id);
}

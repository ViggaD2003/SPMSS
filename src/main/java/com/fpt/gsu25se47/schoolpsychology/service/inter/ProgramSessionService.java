package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;

public interface ProgramSessionService {
    ProgramSessionResponse createProgramSession(CreateProgramSessionRequest request);
}

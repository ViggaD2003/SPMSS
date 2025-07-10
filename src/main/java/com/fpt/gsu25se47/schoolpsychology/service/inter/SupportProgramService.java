package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;

public interface SupportProgramService {
    SupportProgramResponse createSupportProgram(CreateSupportProgramRequest request);
}

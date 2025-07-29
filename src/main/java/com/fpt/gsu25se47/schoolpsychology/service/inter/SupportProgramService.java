package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SupportProgramService {
    SupportProgramResponse createSupportProgram(SupportProgramRequest request, HttpServletRequest servletRequest);
    SupportProgramResponse getSupportProgramById(Integer id);
    List<SupportProgramResponse> getAllSupportPrograms();
//    void deleteSupportProgram(Integer id);
    SupportProgramResponse updateSupportProgram(Integer id, SupportProgramRequest request);
}

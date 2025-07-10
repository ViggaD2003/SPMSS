package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SupportProgramMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SupportProgramServiceImpl implements SupportProgramService {

    private final SupportProgramMapper supportProgramMapper;
    private final ProgramSessionRepository programSessionRepository;
    private final ProgramRegistrationRepository programRegistrationRepository;
    private final SupportProgramRepository supportProgramRepository;
    private final ProgramSurveyRepository programSurveyRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public SupportProgramResponse createSupportProgram(CreateSupportProgramRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Category not found with ID: " + request.getCategoryId()));

        SupportProgram supportProgram = supportProgramMapper.toSupportProgram(request, category);

        SupportProgram supportProgramCreated = supportProgramRepository.save(supportProgram);

        return supportProgramMapper.toSupportProgramResponse(supportProgramCreated);
    }
}

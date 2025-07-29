package com.fpt.gsu25se47.schoolpsychology.service.impl;


import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SupportProgramMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupportProgramServiceImpl implements SupportProgramService {

    private final SupportProgramMapper supportProgramMapper;
    private final ProgramParticipantRepository programParticipantRepository;
    private final SupportProgramRepository supportProgramRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyService surveyService;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public SupportProgramResponse createSupportProgram(SupportProgramRequest request, HttpServletRequest servletRequest) {

        Optional<?> surveyId = surveyService.addNewSurvey(request.getAddNewSurveyDto(), servletRequest);
        if (surveyId.isEmpty()) {
            throw new RuntimeException("Could not add new survey");
        }

        Survey survey = surveyRepository.findById(surveyId.hashCode()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find survey")
        );

        Category category = survey.getCategory();

        Account account = accountRepository.findById(request.getHostedBy()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find account")
        );



        SupportProgram supportProgram = supportProgramMapper.mapSupportProgram(request);
        supportProgram.setCategory(category);
        supportProgram.setHostedBy(account);
        supportProgram.setSurvey(survey);
        if(request.getStartTime().isEqual(LocalDateTime.now())){
            supportProgram.setStatus(ProgramStatus.ACTIVE);
        }

        return supportProgramMapper.mapSupportProgramResponse(supportProgramRepository.save(supportProgram));
    }


    @Override
    public SupportProgramResponse getSupportProgramById(Integer id) {

        return supportProgramMapper.toSupportProgramResponse(
                getSupportProgram(id)
        );
    }

    @Override
    public List<SupportProgramResponse> getAllSupportPrograms() {




//        return supportPrograms
//                .stream()
//                .map(supportProgramMapper::toSupportProgramResponse)
//                .toList();
    }

    @Override
    public void deleteSupportProgram(Integer id) {

        SupportProgram supportProgram = getSupportProgram(id);

        supportProgramRepository.delete(supportProgram);
    }

    @Override
    public SupportProgramResponse updateSupportProgram(Integer id, SupportProgramRequest request) {

        SupportProgram supportProgram = getSupportProgram(id);

        if (!Objects.equals(request.getCategoryId(), supportProgram.getCategory().getId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Category not found with ID: " + request.getCategoryId()));
            supportProgram.setCategory(category);
        }

        supportProgramMapper.updateSupportProgramFromRequest(request, supportProgram);

        SupportProgram supportProgramUpdated = supportProgramRepository.save(supportProgram);

        return supportProgramMapper.toSupportProgramResponse(supportProgramUpdated);
    }

    private SupportProgram getSupportProgram(Integer id) {

        return supportProgramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Support program not found with ID: " + id
                ));
    }
}

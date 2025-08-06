package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.CaseMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.CategoryMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.DashBoardMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    private final SurveyRepository surveyRepository;

    private final AppointmentRepository appointmentRepository;

    private final SupportProgramRepository supportProgramRepository;

    private final CategoryRepository categoryRepository;

    private final LevelRepository levelRepository;

    private final AccountRepository accountRepository;

    private final CaseRepository caseRepository;

    private final DashBoardMapper dashBoardMapper;

    private final CaseMapper caseMapper;

    @Override
    public Optional<?> managerDashboard() {
        Overview overview = Overview.builder()
                .totalStudents(accountRepository.findAllWithRoleStudent().size())
                .totalPrograms(supportProgramRepository.findAll().size())
                .totalSurveys(surveyRepository.findAll().size())
                .totalAppointments(appointmentRepository.findAll().size())
                .activeCases(caseRepository.findAllActiveCases().size())
                .build();

        List<Category> categories = categoryRepository.findAll();

        List<ActivityByCategory> activityByCategories = categories.stream().map(dashBoardMapper::mapToActivityByCategory).toList();

        List<SurveyLevelByCategory> surveyLevelByCategories = categories.stream().map(dashBoardMapper::mapToSurveyLevelByCategory).toList();

        List<CaseGetAllResponse> activeCasesList = caseRepository.findAllActiveCases()
                .stream()
                .map(c -> caseMapper.mapToCaseGetAllResponse(c, null)).toList();

        return Optional.of(ManagerDashboard.builder()
                        .overview(overview)
                        .activityByCategories(activityByCategories)
                        .surveyLevelByCategories(surveyLevelByCategories)
                        .activeCasesList(activeCasesList)
                .build());
    }
}

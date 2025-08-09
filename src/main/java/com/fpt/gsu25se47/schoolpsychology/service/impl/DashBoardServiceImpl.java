package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.CaseMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Manager.DashBoardMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.DashBoardService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    private final SurveyRepository surveyRepository;

    private final AppointmentRepository appointmentRepository;

    private final SupportProgramRepository supportProgramRepository;

    private final CategoryRepository categoryRepository;

    private final AccountRepository accountRepository;

    private final CaseRepository caseRepository;

    private final DashBoardMapper dashBoardMapper;

    private final CaseMapper caseMapper;

    @Override
    public ManagerDashboard managerDashboard() {
        OverviewManager overview = OverviewManager.builder()
                .totalStudents(accountRepository.findAllWithRoleStudent().size())
                .totalPrograms(supportProgramRepository.findAll().size())
                .totalSurveys(surveyRepository.findAll().size())
                .totalAppointments(appointmentRepository.findAll().size())
                .activeCases(caseRepository.findAllActiveCases(null).size())
                .build();

        List<Category> categories = categoryRepository.findAll();

        List<ActivityByCategory> activityByCategories = categories.stream().map(dashBoardMapper::mapToActivityByCategory).toList();

        List<SurveyLevelByCategory> surveyLevelByCategories = categories.stream().map(dashBoardMapper::mapToSurveyLevelByCategory).toList();

        List<CaseGetAllResponse> activeCasesList = caseRepository.findAllActiveCases(null)
                .stream()
                .map(c -> caseMapper.mapToCaseGetAllResponse(c, null)).toList();

        return ManagerDashboard.builder()
                .overview(overview)
                .activityByCategories(activityByCategories)
                .surveyLevelByCategories(surveyLevelByCategories)
                .activeCasesList(activeCasesList).build();
    }

    @Override
    public CounselorDashboard counselorDashboard() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));


            OverviewCounselor overview = OverviewCounselor.builder()
                    .myActiveCases(caseRepository.findAllActiveCases(account.getId()).size())
                    .myAppointmentsThisMonth(appointmentRepository.findMyHostedAppointmentsThisMonth(account.getId()).size())
                    .mySurveysReviewed(surveyRepository.findByAccountId(account.getId()).size())
                    .programsReferred(supportProgramRepository.findAllByHostedBy(account.getId()).size())
                    .build();

            List<UpcomingAppointments> upcomingAppointments = appointmentRepository.findAllByHostByWithStatusConfirmed(account.getId())
                    .stream().map(dashBoardMapper::mapToUpcomingAppointment).toList();

            List<CaseByCategory> caseByCategories = caseRepository.findCaseStatsByCategory(account.getId());

            List<CaseGetAllResponse> activeCasesList = caseRepository.findAllActiveCases(account.getId())
                    .stream()
                    .map(c -> caseMapper.mapToCaseGetAllResponse(c, null)).toList();

            return CounselorDashboard.builder()
                    .overview(overview)
                    .upcomingAppointments(upcomingAppointments)
                    .caseByCategory(caseByCategories)
                    .acitveCaseList(activeCasesList)
                    .build();

        } catch (Exception e){
            log.error("Failed to view counselor dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("Could not view counselor dashboard");
        }
    }


}

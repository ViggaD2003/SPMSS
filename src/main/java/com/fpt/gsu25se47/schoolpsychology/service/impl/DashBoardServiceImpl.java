package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AppointmentStatic;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.CaseMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Manager.DashBoardMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.DashBoardService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    private final MentalEvaluationRepository mentalEvaluationRepository;
    private final ProgramParticipantRepository programParticipantRepository;

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

        } catch (Exception e) {
            log.error("Failed to view counselor dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("Could not view counselor dashboard");
        }
    }

    @Override
    public StudentDashboard studentDashboard(LocalDate startDate, LocalDate endDate, Integer studentId) {
        List<MentalEvaluation> mentalEvaluations = mentalEvaluationRepository.findEvaluationsByTypeAndDate("survey", startDate.atStartOfDay(), endDate.atStartOfDay(), studentId);
        List<DataSet> surveyDataSets = mentalEvaluations.stream()
                .filter(item -> item.getWeightedScore() > 0)
                .map(this::mapToDataSet).toList();

        int surveyCount = surveyRepository
                .findUnansweredExpiredSurveysByAccountId(studentId).size();

        SurveyStatic surveyStatic = SurveyStatic.builder()
                .activeSurveys(surveyCount)
                .dataSet(surveyDataSets)
                .numberOfSkips(surveyRepository.countSurveySkip(studentId, startDate.atStartOfDay(), endDate.atStartOfDay()))
                .completedSurveys(surveyDataSets.size())
                .build();


        List<DataSet> appointmentDataSets = mentalEvaluationRepository.findEvaluationsByTypeAndDate("appointment", startDate.atStartOfDay(), endDate.atStartOfDay(), studentId)
                .stream().map(this::mapToDataSet).toList();

        AppointmentStatic appointmentStatic = AppointmentStatic.builder()
                .activeAppointments(appointmentRepository.findByBookedForAndStatus(studentId, List.of(AppointmentStatus.CONFIRMED), startDate.atStartOfDay(), endDate.atStartOfDay()).size())
                .completedAppointments(appointmentRepository.findByBookedForAndStatus(studentId, List.of(AppointmentStatus.COMPLETED), startDate.atStartOfDay(), endDate.atStartOfDay()).size())
                .numOfAbsent(appointmentRepository.findByBookedForAndStatus(studentId, List.of(AppointmentStatus.ABSENT), startDate.atStartOfDay(), endDate.atStartOfDay()).size())
                .numOfCancel(appointmentRepository.findByBookedForAndStatus(studentId, List.of(AppointmentStatus.CANCELED), startDate.atStartOfDay(), endDate.atStartOfDay()).size())
                .dataSet(appointmentDataSets)
                .build();

        List<DataSet> programDataSets = mentalEvaluationRepository.findEvaluationsByTypeAndDate("program", startDate.atStartOfDay(), endDate.atStartOfDay(), studentId)
                .stream().map(this::mapToDataSet).toList();

        List<ProgramParticipants> participants = programParticipantRepository.findByStudentIdAndJoinAtBetween(studentId, startDate.atStartOfDay(), endDate.atStartOfDay());

        ProgramSupportStatic supportStatic = ProgramSupportStatic.builder()
                .activePrograms(participants.stream().filter(pp -> (pp.getStatus() == RegistrationStatus.ENROLLED || pp.getStatus() == RegistrationStatus.ACTIVE)).toList().size())
                .completedPrograms(participants.stream().filter(pp -> pp.getStatus().equals(RegistrationStatus.COMPLETED)).toList().size())
                .numOfAbsent(participants.stream().filter(pp -> pp.getStatus().equals(RegistrationStatus.ABSENT)).toList().size())
                .dataSet(programDataSets)
                .build();

        OverviewStudent overview = OverviewStudent.builder()
                .totalSurveys(surveyCount + mentalEvaluations.size())
                .totalAppointments((int) appointmentRepository.findAllByBookedFor(studentId).stream().filter(item ->
                        !item.getCreatedDate().isBefore(startDate.atStartOfDay())
                                && !item.getCreatedDate().isAfter(endDate.atStartOfDay())).count()
                )
                .totalPrograms((int) supportProgramRepository.findByStudentId(studentId).stream().filter(item ->
                        !item.getStartTime().isBefore(startDate.atStartOfDay()) &&
                                !item.getEndTime().isAfter(endDate.atStartOfDay())).count())
                .totalCases(caseRepository.countAllClosedCases(studentId, startDate.atStartOfDay(), endDate.atStartOfDay()))
                .build();

        MentalEvaluationStatic mentalEvaluationStatic = MentalEvaluationStatic.builder()
                .program(supportStatic)
                .appointment(appointmentStatic)
                .survey(surveyStatic)
                .build();

        return StudentDashboard.builder()
                .overview(overview)
                .mentalStatistic(mentalEvaluationStatic)
                .build();
    }

    private DataSet mapToDataSet(MentalEvaluation evaluation) {
        if (evaluation == null) return null;

        return DataSet.builder()
                .score(evaluation.getWeightedScore())
                .createdAt(evaluation.getLatestEvaluatedAt())
                .build();
    }
}

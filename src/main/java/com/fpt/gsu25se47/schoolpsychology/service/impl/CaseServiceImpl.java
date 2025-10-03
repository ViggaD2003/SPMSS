package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCaseRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AppointmentStatic;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.CaseMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    private final AccountRepository accountRepository;

    private final LevelRepository levelRepository;

    private final SurveyCaseLinkRepository surveyCaseLinkRepository;

    private final SurveyRepository surveyRepository;

    private final ChatService chatService;

    private final CaseMapper caseMapper;
    private final AppointmentRepository appointmentRepository;
    private final ProgramParticipantRepository programParticipantRepository;
    private final NotificationService notificationService;
    private final SurveyRecordRepository surveyRecordRepository;

    @Override
    @Transactional
    public Optional<?> createCase(AddNewCaseDto dto) {
        // Lấy thông tin student
        Account student = accountRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Kiểm tra role (nên dùng != cho rõ ràng)
        if (student.getRole() != Role.STUDENT) {
            throw new IllegalArgumentException("Account is not STUDENT");
        }

        // Kiểm tra xem student có đang "free"
        if (!caseRepository.isStudentFreeFromOpenCases(dto.getStudentId())) {
            throw new IllegalArgumentException("Student is not available to open cases");
        }

        // Lấy người tạo case
        Account createdBy = accountRepository.findById(dto.getCreateBy())
                .orElseThrow(() -> new IllegalArgumentException("Create By not found"));

        // Lấy level ban đầu và hiện tại
        Level initialLevel = levelRepository.findById(dto.getInitialLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Initial Level not found"));

        Level currentLevel = levelRepository.findById(dto.getCurrentLevelId())
                .orElseThrow(() -> new IllegalArgumentException("Current Level not found"));

        // Tạo case
        Cases cases = Cases.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(dto.getNotify() ? Status.NEW : Status.CONFIRMED)
                .progressTrend(dto.getProgressTrend())
                .createBy(createdBy)
                .notify(dto.getNotify())
                .student(student)
                .initialLevel(initialLevel)
                .currentLevel(currentLevel)
                .build();

        chatService.createChatRoom(caseRepository.save(cases).getId(), dto.getNotify());

        // Gửi notification cho student
        NotiResponse studentRes = notificationService.saveNotification(
                NotiRequest.builder()
                        .title("Bạn đã được tạo case mới")
                        .content("Case " + cases.getTitle())
                        .username(student.getEmail())
                        .notificationType("CASE")
                        .relatedEntityId(cases.getId())
                        .build()
        );

        notificationService.sendNotification(student.getEmail(), "/queue/notifications", studentRes);

        // Gửi notification cho phụ huynh
        if (dto.getNotify()){
            student.getStudent().getRelationships().forEach(relationship -> {
                NotiResponse parentRes = notificationService.saveNotification(
                        NotiRequest.builder()
                                .title("Con bạn đã được tạo case mới")
                                .content("Case " + cases.getTitle())
                                .username(relationship.getGuardian().getAccount().getEmail())
                                .notificationType("CASE")
                                .relatedEntityId(cases.getId())
                                .build()
                );

                notificationService.sendNotification(relationship.getGuardian().getAccount().getEmail(), "/queue/notifications", parentRes);
            });
        }


        NotiResponse managerRes = notificationService.saveNotification(
                NotiRequest.builder()
                        .title("Teacher " + createdBy.getEmail() + " mới tạo 1 case mới")
                        .content("Case " + cases.getTitle())
                        .username("namcaonguyen41@gmail.com")
                        .notificationType("CASE")
                        .relatedEntityId(cases.getId())
                        .build()
        );

        notificationService.sendNotification("namcaonguyen41@gmail.com", "/queue/notifications", managerRes);

        return Optional.of("Case created!");
    }


    @Override
    public Optional<?> assignCounselor(Integer counselorId, List<Integer> caseId) {
        Account account = accountRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        caseId.forEach(id -> {
            Cases cases = caseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cases is not found"));

            cases.setCounselor(account);
            cases.setStatus(Status.IN_PROGRESS);

            caseRepository.save(cases);
        });

        return Optional.of("Case assigned successfully !");
    }

    @Override
    public List<CaseGetAllResponse> getAllCases(List<String> statusCase, Integer categoryId, Integer surveyId, Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        List<Cases> filteredCases = caseRepository.findAllCasesByRoleAndAccountWithStatusSorted(account.getRole().name(), account.getId(), statusCase, statusCase.size(), categoryId);

        return
                filteredCases.stream()
                        .map(c -> caseMapper.mapToCaseGetAllResponse(c, surveyId))
                        .toList();
    }

    @Override
    public Optional<?> getDetailById(Integer caseId) {
        Cases cases = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found"));

        // 1. Handle surveys
        // 1. Handle surveys (dựa trên record thuộc case window)
        List<SurveyRecord> surveyRecordsInThisCase =
                surveyRecordRepository.findAllRecordsBelongToCaseWindow(caseId);

// Đếm skip / not-skip sau khi đã lọc đúng case
        int numberOfSkips = (int) surveyRecordsInThisCase.stream()
                .filter(sr -> Boolean.TRUE.equals(sr.getIsSkipped()))
                .count();

        int numberOfNotSkips = (int) surveyRecordsInThisCase.stream()
                .filter(sr -> !Boolean.TRUE.equals(sr.getIsSkipped()))
                .count();

// DataSet cho chart
        List<DataSet> surveyDataSets = surveyRecordsInThisCase.stream()
                .filter(sr -> sr.getTotalScore() != null && sr.getTotalScore() > 0 && sr.getLevel() != null)
                .map(sr -> mapToDataSet(sr.getMentalEvaluation()))
                .filter(Objects::nonNull)
                .toList();

// Số survey đang active (giữ logic cũ nếu bạn đang dùng để hiển thị “đang assign”)
        SurveyStatic surveyStatic = SurveyStatic.builder()
                .activeSurveys(surveyRepository.findAllSurveyWithLinkActiveByCaseId(caseId).size())
                .completedSurveys(numberOfNotSkips)
                .numberOfSkips(numberOfSkips)
                .dataSet(surveyDataSets)
                .build();


        // 2. Handle appointments
        List<Appointment> appointments = appointmentRepository.findAllByCaseId(caseId);
        int numOfAbsent =
                (int) appointments.stream()
                .filter(appt -> AppointmentStatus.ABSENT == appt.getStatus())
                .count();

        int numOfActive = (int) appointments.stream()
                .filter(appt -> AppointmentStatus.CONFIRMED == appt.getStatus() || AppointmentStatus.IN_PROGRESS == appt.getStatus())
                .count();

        int numOfCompleted =  (int) appointments.stream()
                .filter(appt -> AppointmentStatus.COMPLETED == appt.getStatus())
                .count();

        List<DataSet> appointmentDataSets = appointments.stream()
                .map(appt -> mapToDataSet(appt.getMentalEvaluation()))
                .filter(Objects::nonNull)
                .toList();

        AppointmentStatic appointmentStatic = AppointmentStatic.builder()
                .activeAppointments(numOfActive)
                .completedAppointments(numOfCompleted)
                .numOfAbsent(numOfAbsent)
                .dataSet(appointmentDataSets)
                .build();

        // 3. Handle Program Support
        List<ProgramParticipants> participants = programParticipantRepository.findAllByCaseId(caseId);
        numOfAbsent = (int) participants.stream().filter(pp -> RegistrationStatus.ABSENT.equals(pp.getStatus()))
                .count();

        numOfActive = (int) participants.stream().filter(pp -> RegistrationStatus.ENROLLED.equals(pp.getStatus()) || RegistrationStatus.ACTIVE.equals(pp.getStatus()))
                .count();


        numOfCompleted = (int) participants.stream().filter(pp -> RegistrationStatus.COMPLETED.equals(pp.getStatus()))
                .count();

        List<MentalEvaluation> mentalEvaluationOfPp = participants.stream().map(ProgramParticipants::getMentalEvaluation).toList();

        List<DataSet> programSupportDataSets = mentalEvaluationOfPp.stream()
                .map(this::mapToDataSet)
                .filter(Objects::nonNull)
                .toList();

        ProgramSupportStatic programSupportStatic = ProgramSupportStatic.builder()
                .activePrograms(numOfActive)
                .numOfAbsent(numOfAbsent)
                .completedPrograms(numOfCompleted)
                .dataSet(programSupportDataSets)
                .build();

        // 4. Compose final grouped data
        MentalEvaluationStatic evaluationStatic = MentalEvaluationStatic.builder()
                .survey(surveyStatic)
                .appointment(appointmentStatic)
                .program(programSupportStatic)
                .build();

        CaseGetDetailResponse response = caseMapper
                .mapCaseGetDetailResponse(cases, evaluationStatic);

        return Optional.of(response);
    }


    @Override
    public Optional<?> addSurveyCaseLink(List<Integer> caseIds, Integer surveyId) {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account assignedBy = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            List<Cases> filteredCases = caseRepository.findAll().stream()
                    .filter(item -> {
                        if(caseIds.contains(item.getId())){
                            if(item.getStudent().getStudent().getIsEnableSurvey()){
                                return true;
                            } else {
                                throw new RuntimeException(item.getStudent().getFullName() + " is not enable survey");
                            }
                        } else {
                            return false;
                        }
                    })
                    .toList();

            if(filteredCases.isEmpty()) {
                throw new BadRequestException("No case found");
            }

            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

            if(survey.getSurveyType() != SurveyType.FOLLOWUP){
                throw new BadRequestException("Survey type is not followup");
            }

            filteredCases.forEach(item -> {
                SurveyCaseLink surveyCaseLink = surveyCaseLinkRepository.existsBySurveyIdAndCaseId(surveyId, item.getId());
                if(surveyCaseLink != null){
                    if(!surveyCaseLink.getIsActive()){
                        surveyCaseLink.setIsActive(true);
                        surveyCaseLinkRepository.save(surveyCaseLink);
                    } else {
                        throw new RuntimeException("Survey case link still active");
                    }
                } else {
                    surveyCaseLinkRepository.save(SurveyCaseLink.builder()
                            .cases(item)
                            .survey(survey)
                            .assignedBy(assignedBy)
                            .isActive(true)
                            .build());
                }

                NotiResponse studentRes = notificationService.saveNotification(
                        NotiRequest.builder()
                                .title("Bạn có bài survey mới")
                                .content("Survey " + survey.getTitle())
                                .username(item.getStudent().getEmail())
                                .notificationType("SURVEY")
                                .relatedEntityId(survey.getId())
                                .build()
                );

                notificationService.sendNotification(item.getStudent().getEmail(), "/queue/notifications", studentRes);
            });

            return Optional.of("Added survey case link successfully !");
        } catch (Exception e) {
            log.error("Failed to create survey case link: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<?> removeSurveyCaseLink(List<Integer> caseIds) {
        List<SurveyCaseLink> surveyCaseLinks = surveyCaseLinkRepository.findAll();
        List<SurveyCaseLink> filteredSurveyCaseLink = surveyCaseLinks.stream()
                .filter(item -> caseIds.contains(item.getCases().getId()))
                .toList();

        filteredSurveyCaseLink.forEach(item -> {
            item.setIsActive(false);
            item.setRemoveAt(LocalDate.now());
            surveyCaseLinkRepository.save(item);
        });

        return Optional.of("Removed survey case link successfully !");
    }

    @Override
    @Transactional
    public Optional<?> removeSurveyByCaseId(List<Integer> caseIds, Integer surveyId) {
        caseIds.forEach(item -> {
            if(surveyRecordRepository.isSurveyRecordCaseByCaseId(item, surveyId) == 1){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, item + " already have result of this survey. Can not remove !");
            }
        });

        List<SurveyCaseLink> surveyCaseLinks = surveyCaseLinkRepository.findAllBySurveyId(surveyId);

        List<SurveyCaseLink> filteredSurveyCaseLink = surveyCaseLinks.stream()
                .filter(item -> caseIds.contains(item.getCases().getId()))
                .toList();

        filteredSurveyCaseLink.forEach(item -> {
            item.setIsActive(false);
            item.setRemoveAt(LocalDate.now());
            surveyCaseLinkRepository.save(item);
        });

        return Optional.of("Removed survey case link successfully !");
    }

    @Override
    public Optional<CaseGetAllResponse> updateCase(Integer caseId, UpdateCaseRequest request) {
        Cases cases = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found"));

        if (request.getStatus() == Status.CLOSED){
            cases.getSurveyCaseLinks().forEach(item -> {
                item.setRemoveAt(LocalDate.now());
                item.setIsActive(false);
                surveyCaseLinkRepository.save(item);
            });
        }
        cases.setStatus(request.getStatus());
        cases.setPriority(request.getPriority());
        cases.setProgressTrend(request.getProgressTrend());
        cases.setCurrentLevel(levelRepository.findById(request.getCurrentLevelId()).orElseThrow(() -> new IllegalArgumentException("Current level not found")));
        return Optional.of(caseMapper.mapToCaseGetAllResponse(caseRepository.save(cases), null));
    }

    private DataSet mapToDataSet(MentalEvaluation evaluation) {
        if (evaluation == null) return null;

        return DataSet.builder()
                .score(evaluation.getWeightedScore())
                .createdAt(evaluation.getLatestEvaluatedAt())
                .build();
    }

}

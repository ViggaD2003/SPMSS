package com.fpt.gsu25se47.schoolpsychology.service.impl;


import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.ParticipantMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramParticipantsMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SupportProgramMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SurveyMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.*;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SupportProgramServiceImpl implements SupportProgramService {

    private final SupportProgramMapper supportProgramMapper;
    private final ProgramParticipantsMapper programParticipantsMapper;
    private final ProgramParticipantRepository programParticipantRepository;
    private final SupportProgramRepository supportProgramRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyService surveyService;
    private final AccountRepository accountRepository;
    private final CaseRepository caseRepository;
    private final ParticipantMapper participantMapper;
    private final SurveyRecordService surveyRecordService;
    private final MentalEvaluationService mentalEvaluationService;
    private final FileUploadService fileUploadService;
    private final SurveyRecordRepository surveyRecordRepository;
    private final NotificationService notificationService;
    private final AccountService accountService;
    private final SurveyMapper surveyMapper;


    @Override
    @Transactional
    public SupportProgramResponse createSupportProgram(MultipartFile thumbnail, SupportProgramRequest request) throws IOException {

        Integer surveyId = surveyService.addNewSurvey(request.getAddNewSurveyDto());
        if (surveyId == null) {
            throw new RuntimeException("Could not add new survey");
        }

        Survey survey = surveyRepository.findById(surveyId.hashCode()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find survey")
        );

        Category category = survey.getCategory();

        Account account = accountRepository.findById(request.getHostedBy()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find account")
        );
        int maxSp = supportProgramRepository.countProgramsForCounselorByDate(account.getId(), request.getStartTime().truncatedTo(ChronoUnit.DAYS), request.getEndTime().plusDays(1));

        if (maxSp == 1) {
             if (accountService.getCurrentAccount().getRole() == Role.MANAGER) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("This counselor already hosting %d support program(s) on %s. This counselor cannot host more than one program per day.",
                                maxSp, request.getStartTime().toLocalDate()));
            }
        }

        SupportProgram supportProgram = supportProgramMapper.mapSupportProgram(request);
        Map<String, String> rawFile = fileUploadService.uploadFile(thumbnail);
        supportProgram.setThumbnail(rawFile.get("url"));
        supportProgram.setPublicId(rawFile.get("public_id"));
        supportProgram.setCategory(category);
        supportProgram.setHostedBy(account);
        supportProgram.setSurvey(survey);
        supportProgram.setStatus(ProgramStatus.ACTIVE);
        supportProgram.setIsActiveSurvey(false);

        return supportProgramMapper.mapSupportProgramResponse(supportProgramRepository.save(supportProgram));
    }


    @Override
    public SupportProgramDetail getSupportProgramById(Integer id) {
        return supportProgramMapper.mapSupportProgramDetail(
                getSupportProgram(id)
        );
    }

    @Override
    public List<SupportProgramResponse> getAllSupportPrograms() {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account currentAccount = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            switch (currentAccount.getRole()) {
                case COUNSELOR -> {
                    List<SupportProgram> entity = supportProgramRepository.findAllByHostedBy(currentAccount.getId());
                    return entity.stream().map(supportProgramMapper::mapSupportProgramResponse).toList();
                }
                case MANAGER -> {
                    List<SupportProgram> entity = supportProgramRepository.findAll();
                    return entity.stream().map(supportProgramMapper::mapSupportProgramResponse).toList();
                }
                default -> throw new IllegalStateException("Unexpected value: " + currentAccount.getRole());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error getting support programs", e);
        }
    }

    @Override
    @Transactional
    public List<ProgramParticipantsResponse> addParticipantsToSupportProgram(Integer supportProgramId, List<Integer> caseIds) {
        List<Cases> cases = caseRepository.findAll();
        List<Cases> filteredCases = cases.stream().filter(c -> caseIds.contains(c.getId())).toList();
        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId).orElseThrow(() -> new RuntimeException("Could not find support program"));

        List<ProgramParticipants> listRegistered = new ArrayList<>();

        filteredCases.forEach(c -> {
            if (programParticipantRepository.checkAlreadyRegisterInDay(c.getStudent().getId(), supportProgram.getStartTime().toLocalDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        c.getStudent().getFullName() + " has already registered to another program in today");
            }

            ProgramParticipants participant = programParticipantRepository.save(ProgramParticipants.builder()
                    .program(supportProgram)
                    .student(c.getStudent())
                    .cases(c)
                    .joinAt(LocalDateTime.now())
                    .status(RegistrationStatus.ENROLLED)
                    .finalScore(0f)
                    .build());

            listRegistered.add(participant);

            NotiResponse programSupport = notificationService.saveNotification(
                    NotiRequest.builder()
                            .title("Bạn đã được thêm chương trình hỗ trợ mới")
                            .content("Chương trình hỗ trợ " + supportProgram.getName())
                            .username(participant.getStudent().getEmail())
                            .notificationType("PROGRAM")
                            .relatedEntityId(supportProgram.getId())
                            .build()
            );

            notificationService.sendNotification(participant.getStudent().getEmail(), "/queue/notifications", programSupport);
        });

        return listRegistered.stream().map(participantMapper::mapToProgramParticipantsResponse).toList();
    }

    @Override
    @Transactional
    public Optional<?> saveSurveySupportProgram(Integer programId, Integer studentId, CreateSurveyRecordDto createSurveyRecordDto) {
        try {
            Survey survey = surveyRepository.findById(createSurveyRecordDto.getSurveyId())
                    .orElseThrow(() -> new BadRequestException("Survey not found"));

            ProgramParticipants participant = programParticipantRepository.findByStudentId(studentId, programId);

            if (!survey.getId().equals(participant.getProgram().getSurvey().getId())) {
                throw new BadRequestException("Survey does not belong to the program you're enrolled in");
            }

            SurveyRecordDetailResponse surveyRecordDetailResponse;
            if (!surveyRecordRepository.isEntrySurveyRecordByStudentId(studentId, participant.getProgram().getId())) {
                participant.setStatus(RegistrationStatus.ACTIVE);
                surveyRecordDetailResponse = surveyRecordService.createSurveyRecord(createSurveyRecordDto, SurveyRecordIdentify.ENTRY);

                participant.setFinalScore(surveyRecordDetailResponse.getTotalScore());
            } else {
                participant.setStatus(RegistrationStatus.COMPLETED);
                surveyRecordDetailResponse = surveyRecordService.createSurveyRecord(createSurveyRecordDto, SurveyRecordIdentify.EXIT);
                participant.setFinalScore(surveyRecordDetailResponse.getTotalScore() - participant.getFinalScore());
            }

            if (programParticipantRepository.hasParticipantCompletedSurveyTwice(participant.getId())) {
                MentalEvaluation mentalEvaluation = mentalEvaluationService.createMentalEvaluationWithContext(null, null, participant);
                participant.setMentalEvaluation(mentalEvaluation);
                programParticipantRepository.save(participant);
            }

            return Optional.of(surveyRecordDetailResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error creating survey record", e);
        }
    }

    @Transactional
    @Override
    public RegisterProgramParticipantResponse registerStudentToSupportProgram(Integer supportProgramId) {

        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Support Program not found for ID: " + supportProgramId));

        Account account = accountService.getCurrentAccount();
        Student student = account.getStudent();

        if (supportProgram.getStatus() != ProgramStatus.ACTIVE) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This support program status is not ACTIVE");
        }

        if (programParticipantRepository.findByStudentId(student.getId(), supportProgramId) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Student is already registered to this program for studentId: " + student.getId());
        }

        if (student.getAccount().getStudentCases().stream()
                .anyMatch(c -> !c.getStatus().equals(Status.CLOSED))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This student is having mental cases in progress");
        }

        if (programParticipantRepository.checkAlreadyRegisterInDay(student.getId(), supportProgram.getStartTime().toLocalDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You have already registered to another program in today");
        }

        List<ProgramParticipants> participants = supportProgram.getProgramRegistrations();

        if (!participants.isEmpty() && participants.size() >= supportProgram.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The number of participants is exceeded");
        }

        ProgramParticipants programParticipants = programParticipantRepository.save(ProgramParticipants.builder()
                .program(supportProgram)
                .student(student.getAccount())
                .joinAt(LocalDateTime.now())
                .status(RegistrationStatus.ENROLLED)
                .finalScore(0f)
                .build());

        NotiResponse programSupport = notificationService.saveNotification(
                NotiRequest.builder()
                        .title("Bạn đã được thêm chương trình hỗ trợ mới")
                        .content("Chương trình hỗ trợ " + supportProgram.getName())
                        .username(student.getAccount().getEmail())
                        .notificationType("PROGRAM")
                        .relatedEntityId(supportProgram.getId())
                        .build()
        );

        notificationService.sendNotification(student.getAccount().getEmail(), "/queue/notifications", programSupport);

        return programParticipantsMapper.toRegisterProgramParticipantResponse(programParticipants);
    }

    @Override
    @Transactional
    public String unRegisterStudentFromSupportProgram(Integer supportProgramId, Integer studentId) {
        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId)
                .orElseThrow(() -> new RuntimeException("Support program not found for ID: " + supportProgramId));

        List<ProgramParticipants> participants = supportProgram.getProgramRegistrations();
        participants.removeIf(p -> {
            if (p.getStudent().getId().equals(studentId)) {
                p.setProgram(null);
                return true;
            }
            return false;
        });
        supportProgram.setProgramRegistrations(participants);
        supportProgramRepository.save(supportProgram);
        return "Successfully unregistered support program";
    }

    @Override
    public SupportProgramStudentDetail getSupportProgramStudentDetailById(Integer supportProgramId, Integer studentId) {
        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId)
                .orElseThrow(() -> new RuntimeException("Support program not found for ID: " + supportProgramId));
        ProgramParticipants programParticipants = programParticipantRepository.findByStudentId(studentId, supportProgramId);
        SupportProgramStudent student = participantMapper.mapToSupportProgramStudent(programParticipants);
        SupportProgramStudentDetail programStudentDetail = supportProgramMapper.mapSupportProgramStudentDetail(supportProgram);
        programStudentDetail.setStudent(student);

        return programStudentDetail;
    }

    @Override
    public List<SupportProgramResponse> getSuggestSupportProgram(Integer studentId) {

        if (caseRepository.existsByStudentId(studentId)) {
            return Collections.emptyList();
        }

        SurveyRecord surveyRecordLatest = surveyRecordRepository.findLatestSurveyRecordByStudentId(studentId);

        List<SupportProgramResponse> recommendSupportProgram;
        if (surveyRecordLatest == null) {
            recommendSupportProgram = supportProgramRepository
                    .findAll().stream().filter(sp -> sp.getStatus() == ProgramStatus.ACTIVE)
                    .map(supportProgramMapper::mapSupportProgramResponse)
                    .toList();

        } else {
            recommendSupportProgram = supportProgramRepository
                    .recommendSupportPrograms(surveyRecordLatest.getSurvey().getCategory().getId())
                    .stream().map(supportProgramMapper::mapSupportProgramResponse)
                    .toList();
        }
        return recommendSupportProgram;
    }

    @Override
    public List<SupportProgramPPResponse> getSupportProgramsByStudentId(Integer studentId) {
        List<SupportProgram> sps = supportProgramRepository.findByStudentId(studentId);
        return sps
                .stream()
                .map(s -> {
                    SupportProgramPPResponse supportProgramPPResponse = supportProgramMapper.toSupportProgramPPResponse(s);

                    s.getProgramRegistrations()
                            .stream()
                            .filter(pp -> Objects.equals(pp.getStudent().getId(), studentId))
                            .findFirst()
                            .ifPresent(student -> supportProgramPPResponse.setRegistrationStatus(student.getStatus()));

                    return supportProgramPPResponse;
                })
                .toList();
    }

    @Override
    public SupportProgramResponse updateStatusSupportProgram(Integer id, ProgramStatus newStatus) {

        SupportProgram supportProgram = getSupportProgram(id);

        supportProgram.setStatus(newStatus);
        return supportProgramMapper.mapSupportProgramResponse(supportProgramRepository.save(supportProgram));
    }

    @Override
    public SupportProgramResponse updateSupportProgram(Integer id, UpdateSupportProgramRequest request) {
        SupportProgram supportProgram = getSupportProgram(id);

        if (supportProgram.getStatus() != ProgramStatus.ACTIVE) {
            throw new RuntimeException("Support program can not be update with status " + supportProgram.getStatus());
        }

        supportProgram.setDescription(request.getDescription());
        supportProgram.setName(request.getName());
        supportProgram.setMaxParticipants(request.getMaxParticipants());
        supportProgram.setStartTime(request.getStartTime());
        supportProgram.setEndTime(request.getEndTime());
        supportProgram.setLocation(request.getLocation());

        Account account = accountRepository.findById(request.getHostedBy())
                .orElseThrow(() -> new RuntimeException("Account not found for hosted by: " + request.getHostedBy()));

        supportProgram.setHostedBy(account);
        return supportProgramMapper.mapSupportProgramResponse(supportProgramRepository.save(supportProgram));
    }

    private SupportProgram getSupportProgram(Integer id) {

        return supportProgramRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Support program not found with ID: " + id
                ));
    }

    @Override
    public String openSurvey(Integer supportProgramId) {
        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId)
                .orElseThrow(() -> new RuntimeException("Support program not found for ID: " + supportProgramId));
        if (!supportProgram.getIsActiveSurvey()) {
            supportProgram.setIsActiveSurvey(true);
            supportProgramRepository.save(supportProgram);
            return "Successfully opened support program";
        } else {
            supportProgram.setIsActiveSurvey(false);
            supportProgramRepository.save(supportProgram);
            return "Successfully closed support program";
        }
    }

    @Override
    public List<SupportProgramResponse> getAllActiveSupportPrograms() {

        Account account = accountService.getCurrentAccount();

        List<SupportProgram> activeSupportPrograms = supportProgramRepository.findAllActive(account.getId());

        List<SupportProgramResponse> responses = activeSupportPrograms.stream().map(supportProgramMapper::mapSupportProgramResponse)
                .toList();

        return responses;
    }

    @Override
    public String addNewThumbnail(Integer programId, MultipartFile thumbnail) throws IOException {
        SupportProgram supportProgram = supportProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Support program not found for ID: " + programId));

        Map<String, String> thumbnailMap = fileUploadService.uploadFile(thumbnail);

        supportProgram.setThumbnail(thumbnailMap.get("url"));
        supportProgram.setPublicId(thumbnailMap.get("public_id"));
        supportProgramRepository.save(supportProgram);

        return "Add New Thumbnail Successfully";
    }

    @Override
    public String deleteThumbnail(Integer programId, String public_id) {
        SupportProgram supportProgram = supportProgramRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Support program not found for ID: " + programId));

        String public_id_response = fileUploadService.deleteFile(public_id);
        if (!public_id_response.isEmpty() && public_id.equals(public_id_response)) {
            supportProgram.setPublicId(null);
            supportProgram.setThumbnail(null);
            supportProgramRepository.save(supportProgram);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thumbnail not found");
        }
        return "Thumbnail Deleted Successfully";
    }
}

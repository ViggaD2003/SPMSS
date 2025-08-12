package com.fpt.gsu25se47.schoolpsychology.service.impl;


import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.ParticipantMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramParticipantsMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SupportProgramMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordIdentify;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final MentalEvaluationRepository mentalEvaluationRepository;
    private final NotificationService notificationService;
    private final AccountService accountService;


    @Override
    @Transactional
    public SupportProgramResponse createSupportProgram(MultipartFile thumbnail, SupportProgramRequest request) throws IOException {

        Optional<?> surveyId = surveyService.addNewSurvey(request.getAddNewSurveyDto());
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
        String rawFile = fileUploadService.uploadFile(thumbnail);
        supportProgram.setThumbnail(rawFile);
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
    public List<ProgramParticipantsResponse> addParticipantsToSupportProgram(Integer supportProgramId, List<Integer> caseIds) {
        List<Cases> cases = caseRepository.findAll();
        List<Cases> filteredCases = cases.stream().filter(c -> caseIds.contains(c.getId())).toList();
        SupportProgram supportProgram = supportProgramRepository.findById(supportProgramId).orElseThrow(() -> new RuntimeException("Could not find support program"));
        filteredCases.forEach(c -> {
            ProgramParticipants participants = ProgramParticipants.builder()
                    .program(supportProgram)
                    .student(c.getStudent())
                    .cases(c)
                    .joinAt(LocalDateTime.now())
                    .status(RegistrationStatus.ENROLLED)
                    .finalScore(0f)
                    .build();
            programParticipantRepository.save(participants);
        });

        List<ProgramParticipants> participants = programParticipantRepository.findByProgramId(supportProgramId);

        participants.forEach(participant -> {
            NotiResponse programSupport = notificationService.saveNotification(
                    NotiRequest.builder()
                            .title("Bạn đã được thêm chương trình hỗ trợ mới")
                            .content("Chương trình hỗ trợ " + supportProgram.getName())
                            .username(participant.getStudent().getEmail())
                            .notificationType("CASE")
                            .relatedEntityId(supportProgram.getId())
                            .build()
            );

            notificationService.sendNotification(participant.getStudent().getEmail(), "/queue/notifications", programSupport);

        });

        return participants.stream().map(participantMapper::mapToDto).toList();
    }

    @Override
    @Transactional
    public Optional<?> saveSurveySupportProgram(Integer programId, CreateSurveyRecordDto createSurveyRecordDto) {
        try {
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account currentAccount = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            Survey survey = surveyRepository.findById(createSurveyRecordDto.getSurveyId())
                    .orElseThrow(() -> new BadRequestException("Survey not found"));

            ProgramParticipants participant = programParticipantRepository.findByStudentId(currentAccount.getId(), programId);

            if (!survey.getId().equals(participant.getProgram().getSurvey().getId())) {
                throw new BadRequestException("Survey does not belong to the program you're enrolled in");
            }

            if(!surveyRecordRepository.isExistEntrySurveyRecordByStudentId(currentAccount.getId(), participant.getProgram().getId())) {
                surveyRecordService.createSurveyRecord(createSurveyRecordDto, SurveyRecordIdentify.ENTRY);
            } else {
                surveyRecordService.createSurveyRecord(createSurveyRecordDto, SurveyRecordIdentify.EXIT);
            }

            if (programParticipantRepository.hasParticipantCompletedSurveyTwice(participant.getId())) {
                List<SurveyRecord> surveyRecords = surveyRecordRepository.findTwoSurveyRecordsByParticipant(participant.getId());

                SurveyRecord entryRecord = surveyRecords.stream()
                        .filter(sr -> sr.getSurveyRecordIdentify() == SurveyRecordIdentify.ENTRY)
                        .findFirst().orElse(null);

                SurveyRecord exitRecord = surveyRecords.stream()
                        .filter(sr -> sr.getSurveyRecordIdentify() == SurveyRecordIdentify.EXIT)
                        .findFirst().orElse(null);

                if (surveyRecords.size() == 2 && entryRecord != null && exitRecord != null) {
                    float weightScore = (entryRecord.getTotalScore() + exitRecord.getTotalScore()) / 2;

                    MentalEvaluation mentalEvaluation = mentalEvaluationService.createMentalEvaluationWithContext(null, null, participant);
                    mentalEvaluation.setWeightedScore(weightScore);

                    participant.setMentalEvaluation(mentalEvaluation);
                    mentalEvaluationRepository.save(mentalEvaluation);
                    programParticipantRepository.save(participant);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating survey record", e);
        }

        return Optional.of("Successfully created survey record");
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Student is already registered to this program for studentId: " + student.getId());
        }

        if (student.getAccount().getStudentCases().stream()
                .anyMatch(c -> !c.getStatus().equals(Status.CLOSED))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This student is having mental cases in progress");
        }

        List<ProgramParticipants> participants = supportProgram.getProgramRegistrations();

        if (!participants.isEmpty() && participants.size() >= supportProgram.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The number of participants is exceeded");
        }

        ProgramParticipants programParticipants = ProgramParticipants.builder()
                .program(supportProgram)
                .student(student.getAccount())
                .joinAt(LocalDateTime.now())
                .status(RegistrationStatus.ENROLLED)
                .finalScore(0f)
                .build();

        programParticipantRepository.save(programParticipants);

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
            if (p.getStudent().getId().equals(studentId) ) {
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
    public Optional<?> getSuggestSupportProgram(Integer studentId) {

        if (caseRepository.existsByStudentId(studentId)) {
            return Optional.of(Collections.emptyList());
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
        return Optional.of(recommendSupportProgram);
    }

    @Override
    public List<SupportProgramPPResponse> getSupportProgramsByStudentId(Integer studentId) {

        List<SupportProgram> sps =  supportProgramRepository.findByStudentId(studentId);
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
    public SupportProgramResponse updateSupportProgram(Integer id, ProgramStatus newStatus) {

        SupportProgram supportProgram = getSupportProgram(id);

        supportProgram.setStatus(newStatus);
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
        if(!supportProgram.getIsActiveSurvey()){
            supportProgram.setIsActiveSurvey(true);
            supportProgramRepository.save(supportProgram);
            return "Successfully opened support program";
        } else {
            supportProgram.setIsActiveSurvey(false);
            supportProgramRepository.save(supportProgram);
            return "Successfully closed support program";
        }
    }
}
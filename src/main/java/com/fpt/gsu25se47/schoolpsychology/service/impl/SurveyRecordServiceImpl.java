package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.validations.DuplicateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyRecordServiceImpl implements SurveyRecordService {

    private final SurveyRecordRepository surveyRecordRepository;
    private final AccountRepository accountRepository;
    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final AnswerRepository answerRepository;
    private final DuplicateValidator duplicateValidator;
    private final AccountService accountService;
    private final QuestRepository questRepository;


    @Override
    @Transactional
    public SurveyRecordDetailResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {
            Survey survey = surveyRepository.findById(dto.getSurveyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Survey not found with ID: " + dto.getSurveyId()));

            Account account = accountService.getCurrentAccount();

            if(dto.getIsSkipped()){
                SurveyRecord surveyRecord = SurveyRecord.builder()
                        .totalScore(0f)
                        .completedAt(LocalDate.now())
                        .answerRecords(null)
                        .level(null)
                        .isSkipped(dto.getIsSkipped())
                        .survey(survey)
                        .student(account)
                        .round(dto.getRound())
                        .build();
                surveyRecordRepository.save(surveyRecord);
            }

            duplicateValidator.validateAnswerIds(dto.getAnswerRecordRequests());

            List<AnswerRecord> answerRecords = dto.getAnswerRecordRequests()
                    .stream().map(this::mapToAnswerRecord).toList();

            Category category = survey.getCategory();

            List<Level> levels = category.getLevels();

            Level matchLevel = levels.stream()
                    .filter(level -> level.getMinScore() <= dto.getTotalScore() && level.getMaxScore() <= dto.getTotalScore())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Total Score do not match any levels"));


                SurveyRecord surveyRecord = SurveyRecord.builder()
                        .totalScore(0f)
                        .completedAt(LocalDate.now())
                        .isSkipped(dto.getIsSkipped())
                        .answerRecords(answerRecords)
                        .survey(survey)
                        .student(account)
                        .level(matchLevel)
                        .round(dto.getRound())
                        .build();

                if(!surveyRecord.getAnswerRecords().isEmpty()){
                    surveyRecord.getAnswerRecords()
                            .forEach(item -> item.setSurveyRecord(surveyRecord));
                }

                SurveyRecord saved = surveyRecordRepository.save(surveyRecord);
                return this.mapToSurveyRecordResponse(saved);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SurveyRecordGetAllResponse> getAllSurveyRecordById(int accountId, Pageable pageable) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for ID: " + accountId));

        return surveyRecordRepository.findAllByStudentId(account.getId(), pageable)
                .map(this::mapToSurveyRecordGetAllResponse);
    }

    @Override
    public SurveyRecordDetailResponse getSurveyRecordById(int surveyRecordId) {
        SurveyRecord surveyRecord = surveyRecordRepository.findById(surveyRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Survey Record not found with Id: " + surveyRecordId));
        return this.mapToSurveyRecordResponse(surveyRecord);
    }

//    @Override
//    public Page<SurveyRecordDetailResponse> getAllSurveyRecords(Pageable pageable) {
//        return surveyRecordRepository.findAll(pageable)
//                .map(surveyRecord -> {
//                    Student student = getStudent(surveyRecord.getAccount().getId());
//
//                    return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord, student);
//                });
//    }
//
//    private Student getStudent(Integer id) {
//
//        return studentRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
//                        "Student not found with Id: " + id));
//    }

    private AnswerRecord mapToAnswerRecord(SubmitAnswerRecordRequest request) {
        if(request.isSkipped()){
            Question question = questRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Not found question"));
            if(!question.getIsRequired()){
                return AnswerRecord.builder()
                        .question(question)
                        .isSkipped(request.isSkipped())
                        .answer(null)
                        .build();
            } else {
                throw new IllegalArgumentException("Conflict with question require !");
            }
        } else {
           Answer answer = answerRepository.findById(request.getAnswerId())
                   .orElseThrow(() -> new IllegalArgumentException("Not found answer"));
           return AnswerRecord.builder()
                   .question(null)
                   .isSkipped(request.isSkipped())
                   .answer(answer)
                   .build();
        }
    }

    private SurveyRecordGetAllResponse mapToSurveyRecordGetAllResponse(SurveyRecord surveyRecord) {
        return SurveyRecordGetAllResponse.builder()
                .id(surveyRecord.getId())
                .totalScore(surveyRecord.getTotalScore())
                .round(surveyRecord.getRound())
                .isSkipped(surveyRecord.getIsSkipped())
                .completedAt(surveyRecord.getCompletedAt())
                .level(surveyRecord.getLevel() == null ? null : this.mapToLevelResponse(surveyRecord.getLevel()))
                .build();
    }

    private SurveyRecordDetailResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord) {
        return SurveyRecordDetailResponse.builder()
                .id(surveyRecord.getId())
                .totalScore(surveyRecord.getTotalScore())
                .round(surveyRecord.getRound())
                .isSkipped(surveyRecord.getIsSkipped())
                .completedAt(surveyRecord.getCompletedAt())
                .level(surveyRecord.getLevel() == null ? null : this.mapToLevelResponse(surveyRecord.getLevel()))
                .survey(this.mapToSurveyDetailResponse(surveyRecord.getSurvey()))
                .answerRecords(surveyRecord.getAnswerRecords() == null ?
                        null : surveyRecord.getAnswerRecords().stream().map(this::mapToAnswerRecordResponse).toList())
                .build();
    }

    private AnswerRecordResponse mapToAnswerRecordResponse(AnswerRecord answerRecord) {
        if(answerRecord.isSkipped()){
            return AnswerRecordResponse.builder()
                    .id(answerRecord.getId())
                    .questionResponse(this.mapToQuestionDto(answerRecord.getQuestion()))
                    .answerResponse(null)
                    .skipped(answerRecord.isSkipped())
                    .build();
        } else {
            return AnswerRecordResponse.builder()
                    .id(answerRecord.getId())
                    .questionResponse(null)
                    .answerResponse(this.mapToAnswerResponse(answerRecord.getAnswer()))
                    .skipped(answerRecord.isSkipped())
                    .build();
        }
    }


    private LevelResponse mapToLevelResponse(Level level) {
        return LevelResponse.builder()
                .id(level.getId())
                .label(level.getLabel())
                .minScore(level.getMinScore())
                .maxScore(level.getMaxScore())
                .levelType(level.getLevelType())
                .code(level.getCode())
                .description(level.getDescription())
                .symptomsDescription(level.getSymptomsDescription())
                .interventionRequired(level.getInterventionRequired())
                .build();
    }

    private SurveyDetailResponse mapToSurveyDetailResponse(Survey survey) {
        return SurveyDetailResponse.builder()
                .surveyId(survey.getId())
                .createdAt(survey.getCreatedDate())
                .updatedAt(survey.getUpdatedDate())
                .title(survey.getTitle())
                .status(survey.getStatus().name())
                .isRecurring(survey.getIsRecurring())
                .isRequired(survey.getIsRequired())
                .endDate(survey.getEndDate())
                .startDate(survey.getStartDate())
                .description(survey.getDescription())
                .recurringCycle(survey.getRecurringCycle().name())
                .targetGrade(survey.getTargetGradeLevel().name())
                .targetScope(survey.getTargetScope().name())
                .surveyType(survey.getSurveyType().name())
                .category(this.mapToCategorySurveyResponse(survey.getCategory()))
                .round(survey.getRound())
                .questions(survey.getQuestions().stream().map(this::mapToQuestionResponse).toList())
                .build();
    }



    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .description(question.getDescription())
                .isActive(question.getIsActive())
                .isRequired(question.getIsRequired())
                .questionType(question.getQuestionType().name())
                .answers(question.getAnswers().stream().map(this::mapToAnswerResponse).toList())
                .build();
    }

    private QuestionDto mapToQuestionDto(Question question) {
        return QuestionDto.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .description(question.getDescription())
                .isActive(question.getIsActive())
                .isRequired(question.getIsRequired())
                .questionType(question.getQuestionType().name())
                .build();
    }

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .score(answer.getScore())
                .text(answer.getText())
                .build();
    }

    private CategoryResponse mapToCategorySurveyResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .isSum(category.getIsSum())
                .description(category.getDescription())
                .maxScore(category.getMaxScore())
                .minScore(category.getMinScore())
                .questionLength(category.getQuestionLength())
                .isActive(category.getIsActive())
                .severityWeight(category.getSeverityWeight())
                .isLimited(category.getIsLimited())
                .build();
    }
}

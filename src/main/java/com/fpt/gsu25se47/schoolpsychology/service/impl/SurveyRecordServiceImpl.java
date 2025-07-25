package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
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
    private final AnswerRepository answerRepository;
    private final DuplicateValidator duplicateValidator;
    private final AccountService accountService;
    private final QuestRepository questRepository;
    private final AnswerRecordMapper answerRecordMapper;
    private final SurveyRecordMapper surveyRecordMapper;


    @Override
    @Transactional
    public SurveyRecordDetailResponse createSurveyRecord(CreateSurveyRecordDto dto) {
        try {
            Survey survey = surveyRepository.findById(dto.getSurveyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Survey not found with ID: " + dto.getSurveyId()));

            Account account = accountService.getCurrentAccount();

            if(dto.getIsSkipped() && survey.getIsRequired()){
                throw new RuntimeException("Survey is required");
            }

            if(dto.getIsSkipped() && !survey.getIsRequired()) {
                SurveyRecord surveyRecord = SurveyRecord.builder()
                        .totalScore(0f)
                        .completedAt(LocalDate.now())
                        .answerRecords(null)
                        .level(null)
                        .isSkipped(dto.getIsSkipped())
                        .survey(survey)
                        .student(account)
                        .build();
                SurveyRecord saved = surveyRecordRepository.save(surveyRecord);
                return surveyRecordMapper.mapToSurveyRecordResponse(saved);
            }

            duplicateValidator.validateAnswerIds(dto.getAnswerRecordRequests());

            List<AnswerRecord> answerRecords = dto.getAnswerRecordRequests()
                    .stream().map(item -> answerRecordMapper.mapToAnswerRecord(item, questRepository, answerRepository)).toList();

            Category category = survey.getCategory();

            List<Level> levels = category.getLevels();

            Level matchLevel = levels.stream()
                    .filter(level -> level.getMinScore() <= dto.getTotalScore() && level.getMaxScore() <= dto.getTotalScore())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Total Score do not match any levels"));


                SurveyRecord surveyRecord = SurveyRecord.builder()
                        .totalScore(dto.getTotalScore())
                        .completedAt(LocalDate.now())
                        .isSkipped(dto.getIsSkipped())
                        .answerRecords(answerRecords)
                        .survey(survey)
                        .student(account)
                        .level(matchLevel)
                        .build();

                if(!surveyRecord.getAnswerRecords().isEmpty()){
                    surveyRecord.getAnswerRecords()
                            .forEach(item -> item.setSurveyRecord(surveyRecord));
                }

                SurveyRecord saved = surveyRecordRepository.save(surveyRecord);
                return surveyRecordMapper.mapToSurveyRecordResponse(saved);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<SurveyRecordGetAllResponse> getAllSurveyRecordById(SurveyType surveyType, Integer accountId, Pageable pageable) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found for ID: " + accountId));

        return surveyRecordRepository.findAllSurveyRecordsByStudentIdAndSurveyType(account.getId(), surveyType, pageable)
                .map(surveyRecordMapper::mapToSurveyRecordGetAllResponse);
    }

    @Override
    public SurveyRecordDetailResponse getSurveyRecordById(int surveyRecordId) {
        SurveyRecord surveyRecord = surveyRecordRepository.findById(surveyRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Survey Record not found with Id: " + surveyRecordId));
        return surveyRecordMapper.mapToSurveyRecordResponse(surveyRecord);
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







}

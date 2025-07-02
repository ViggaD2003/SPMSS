package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyRecordMapper {

    private final StudentRepository studentRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRecordMapper answerRecordMapper;
    private final StudentMapper studentMapper;
    private final AccountService accountService;

    public SurveyRecord mapToSurveyRecord(CreateSurveyRecordDto dto) {

        Survey survey = surveyRepository.findById(dto.getSurveyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Survey not found with ID: " + dto.getSurveyId()));

        List<AnswerRecord> answerRecords = dto.getAnswerRecordRequests()
                .stream()
                .map(t -> {
                    var createAnswerRecordRequest = CreateAnswerRecordRequest.builder()
                            .submitAnswerRecordRequests(t)
                            .build();
                    return answerRecordMapper.mapToAnswerRecord(createAnswerRecordRequest);
                })
                .toList();

        return SurveyRecord.builder()
                .survey(survey)
                .answerRecords(answerRecords)
                .noteSuggest(dto.getNoteSuggest())
                .level(dto.getLevel())
                .status(dto.getStatus())
                .account(accountService.getCurrentAccount())
                .completedAt(LocalDate.now())
                .totalScore(dto.getTotalScore())
                .build();
    }

    public SurveyRecordResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord) {

        List<AnswerRecordResponse> answerRecordResponses = surveyRecord.getAnswerRecords()
                .stream()
                .map(answerRecordMapper::mapToAnswerRecordResponse)
                .toList();

        Student student = studentRepository.findById(surveyRecord.getAccount().getId()).get();

        return SurveyRecordResponse.builder()
                .id(surveyRecord.getId())
                .surveyId(surveyRecord.getSurvey().getId())
                .answerRecords(answerRecordResponses)
                .totalScore(surveyRecord.getTotalScore())
                .level(surveyRecord.getLevel().name())
                .noteSuggest(surveyRecord.getNoteSuggest())
                .completedAt(surveyRecord.getCompletedAt())
                .studentDto(studentMapper.mapToStudentDto(student))
                .status(surveyRecord.getStatus().name())
                .build();
    }
}
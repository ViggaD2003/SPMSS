package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AnswerRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SurveyRecordMapper {

    private final AnswerRecordMapper answerRecordResponseMapper;
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;

    public SurveyRecordResponse mapToSurveyRecordResponse(SurveyRecord surveyRecord) {

        List<AnswerRecordResponse> answerRecordResponses = surveyRecord.getAnswerRecords()
                .stream()
                .map(answerRecordResponseMapper::mapToAnswerRecordResponse)
                .toList();

        Student student = studentRepository.findById(surveyRecord.getAccount().getId()).get();

        return SurveyRecordResponse.builder()
                .id(surveyRecord.getId())
                .surveyId(surveyRecord.getSurvey().getId())
                .answerRecords(answerRecordResponses)
                .totalScore(surveyRecord.getTotalScore())
                .noteSuggest(surveyRecord.getNoteSuggest())
                .completedAt(surveyRecord.getCompletedAt())
                .studentDto(studentMapper.mapToStudentDto(student))
                .status(surveyRecord.getStatus().name())
                .build();
    }
}

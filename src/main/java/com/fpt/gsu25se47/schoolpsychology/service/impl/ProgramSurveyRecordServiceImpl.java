package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramSurveyRecordServiceImpl implements ProgramSurveyRecordService {

    private final ProgramSurveyRecordRepository programSurveyRecordRepository;

    private final AnswerRepository answerRepository;

    private final ProgramRegistrationRepository programRegistrationRepository;

    private final ProgramSurveyRepository programSurveyRepository;

    private final AnswerRecordRepository answerRecordRepository;

    private final QuestRepository questRepository;


    @Override
    public Optional<?> submitProgramRecord(CreateProgramRecordDto dto) {
        // Bước 1: Load entities gốc
        ProgramRegistration registration = programRegistrationRepository.findById(dto.getProgramRegistrationId())
                .orElseThrow(() -> new RuntimeException("Program Registration Not Found"));

        ProgramSurvey programSurvey = programSurveyRepository.findById(dto.getProgramSurveyId())
                .orElseThrow(() -> new RuntimeException("Program Survey Not Found"));

        // Bước 2: Tạo ProgramRecord
        ProgramRecord programRecord = ProgramRecord.builder()
                .description(dto.getDescription())
                .totalScore(dto.getTotalScore())
                .status(dto.getStatus())
                .summary(dto.getSummary())
                .completeAt(LocalDate.now())
                .programRegistration(registration)
                .programSurvey(programSurvey)
                .build();

        // Bước 3: Map các AnswerRecord và set ProgramRecord
        List<AnswerRecord> answerRecords = dto.getAnswersRecordRequests().stream()
                .map(r -> mapToAnsweredRecord(r, programRecord))
                .toList();

        // Bước 4: Gán AnswerRecords cho ProgramRecord
        programRecord.setAnswerRecords(answerRecords);

        // Bước 5: Validate các câu bắt buộc phải được trả lời
        validateRequiredQuestions(programSurvey.getId(), answerRecords);

        // Bước 6: Save ProgramRecord (cascading sẽ save cả answerRecord)
        ProgramRecord saved = programSurveyRecordRepository.save(programRecord);

        return Optional.of("created successfully");
    }

    @Override
    public Optional<?> showProgramRecord(Integer programSurveyId, Integer registerId) {
        List<ProgramRecord> programRecords = programSurveyRecordRepository
                .findBySupportProgramAndRegistration(programSurveyId, registerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bản ghi chương trình."));

        List<ProgramSurveyRecordDto> dtos = programRecords.stream()
                .map(item -> ProgramSurveyRecordDto.builder()
                        .id(item.getId())
                        .status(item.getStatus().name())
                        .summary(item.getSummary())
                        .totalScore(item.getTotalScore())
                        .description(item.getDescription())
                        .completedAt(item.getCompleteAt())
                        .programSurveyDto(this.mapToProgramSurveyDto(item.getProgramSurvey()))
                        .answerRecords(item.getAnswerRecords().stream()
                                .map(this::mapToAnsweredRecord)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return Optional.of(dtos);
    }


    private AnswerRecordResponse mapToAnsweredRecord(AnswerRecord record) {
        if(record.getAnswer() == null) {
            return AnswerRecordResponse.builder()
                    .id(record.getId())
                    .skipped(record.isSkipped())
                    .answerResponse(null)
                    .questionResponse(this.mapToQuestionResponse(record.getQuestion()))
                    .build();
        } else{
            return AnswerRecordResponse.builder()
                    .id(record.getId())
                    .skipped(record.isSkipped())
                    .answerResponse(this.mapToAnswerResponse(record.getAnswer()))
                    .questionResponse(this.mapToQuestionResponse(record.getAnswer().getQuestion()))
                    .build();
        }
    }


    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getId())
                .updatedAt(question.getUpdatedDate())
                .createdAt(question.getCreatedDate())
                .text(question.getText())
                .moduleType(question.getModuleType().name())
                .description(question.getDescription())
                .isActive(question.isActive())
                .isRequired(question.isRequired())
                .questionType(question.getQuestionType().name())
                .category(mapToCategoryResponse(question.getCategory()))
                .answers(question.getAnswers().stream().map(this::mapToAnswerResponse).toList())
                .build();
    }

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .score(answer.getScore())
                .text(answer.getText())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .code(category.getCode())
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private ProgramSurveyDto mapToProgramSurveyDto(ProgramSurvey programSurvey) {
        return ProgramSurveyDto.builder()
                .id(programSurvey.getId())
                .surveyType(programSurvey.getSurveyType().name())
                .updatedAt(programSurvey.getUpdatedDate())
                .description(programSurvey.getDescription())
                .createdAt(programSurvey.getCreatedDate())
                .name(programSurvey.getName())
                .build();
    }
    private AnswerRecord mapToAnsweredRecord(SubmitAnswerRecordRequest request, ProgramRecord programRecord) {
        if (request.isSkipped()) {
            Question question = questRepository.findById(request.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question Not Found"));

            return AnswerRecord.builder()
                    .isSkipped(true)
                    .question(question)
                    .programRecord(programRecord)
                    .build();
        } else {
            Answer answer = answerRepository.findById(request.getAnswerId())
                    .orElseThrow(() -> new RuntimeException("Answer Not Found"));

            return AnswerRecord.builder()
                    .answer(answer)
                    .isSkipped(false)
                    .programRecord(programRecord)
                    .build();
        }
    }

    private void validateRequiredQuestions(Integer programSurveyId, List<AnswerRecord> submittedRecords) {
        List<Question> requiredQuestions = questRepository.findByProgramSurveyIdAndIsRequiredTrue(programSurveyId);

        for (Question required : requiredQuestions) {
            boolean answered = submittedRecords.stream().anyMatch(ar ->
                    (ar.getAnswer() != null && ar.getAnswer().getQuestion().getId().equals(required.getId())) ||
                            (ar.getQuestion() != null && ar.getQuestion().getId().equals(required.getId()) && !ar.isSkipped())
            );

            if (!answered) {
                throw new RuntimeException("Required question '" + required.getText() + "' was not answered.");
            }
        }
    }


}

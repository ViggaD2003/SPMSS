package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.model.Answer;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramSurvey;
import com.fpt.gsu25se47.schoolpsychology.model.Question;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ProgramSurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramSurveyServiceImpl implements ProgramSurveyService {

    private final ProgramSurveyRepository programSurveyRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Optional<?> addNewPrgSurvey(AddNewProgramSurvey addNewProgramSurvey) {
        return Optional.empty();
    }


    private ProgramSurvey mapToProgramSurvey(AddNewProgramSurvey addNewProgramSurvey) {
        return ProgramSurvey.builder()
                .surveyType(SurveyType.valueOf(addNewProgramSurvey.surveyType()))
                .questions(null)
                .build();
    }

    private Question mapToQuestion(AddNewQuestionDto dto){
        return Question.builder()
                .answers(dto.getAnswers().stream().map(this::mapToAnswer).toList())
                .category(categoryRepository.findById(dto.getCategoryId()).orElse(null))
                .description(dto.getDescription())
                .text(dto.getText())
                .isActive(true)
                .isRequired(dto.isRequired())
                .questionType(dto.getQuestionType())
                .moduleType(dto.getModuleType())
                .build();
    }

    private Answer mapToAnswer(AddNewAnswerDto dto){
        return Answer.builder()
                .score(dto.getScore())
                .text(dto.getText())
                .build();
    }
}

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

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramSurveyServiceImpl implements ProgramSurveyService {

    private final ProgramSurveyRepository programSurveyRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Optional<?> addNewPrgSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSupportId) {

        ProgramSurvey programSurvey = this.mapToProgramSurvey(addNewProgramSurvey, programSupportId);

        programSurvey.getQuestions().forEach(question -> {
           question.setProgramSurvey(programSurvey);

           if(question.getAnswers().isEmpty()){
               throw new IllegalArgumentException("Answers cannot be empty");
           }

            question.getAnswers().forEach(answer -> {
                answer.setQuestion(question);
            });
        });

        programSurveyRepository.save(programSurvey);

        return Optional.of("Successfully added program survey");
    }


    private ProgramSurvey mapToProgramSurvey(AddNewProgramSurvey addNewProgramSurvey, Integer programSupportId) {
        if (addNewProgramSurvey.questionDtos().isEmpty()) {
            throw new IllegalArgumentException("Questions cannot be empty");
        }



        return ProgramSurvey.builder()
                .surveyType(SurveyType.valueOf(addNewProgramSurvey.surveyType()))
//                .program()
                .questions(addNewProgramSurvey.questionDtos().stream().map(this::mapToQuestion).toList())
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

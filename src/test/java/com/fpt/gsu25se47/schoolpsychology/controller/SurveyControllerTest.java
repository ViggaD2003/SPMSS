package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class SurveyControllerTest {

    private static final Logger log = LoggerFactory.getLogger(SurveyControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddNewSurveyDto surveyDto;

    private static AddNewQuestionDto createQuestion(String questionText, int number) {
        return AddNewQuestionDto.builder()
                .text(number + ". " + questionText)
                .description(null)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .isRequired(true)
                .answers(List.of(
                        createAnswer("Không bao giờ", 0),
                        createAnswer("Vài ngày", 1),
                        createAnswer("Hơn một nửa số ngày", 2),
                        createAnswer("Gần như mỗi ngày", 3)
                ))
                .build();
    }

    private static AddNewAnswerDto createAnswer(String text, int score) {
        return AddNewAnswerDto.builder()
                .text(text)
                .score(score)
                .build();
    }

    @BeforeEach
    void initData(){
        surveyDto = AddNewSurveyDto.builder()
                .title("Khảo sát học sinh")
                .description("Khảo sát về tâm lý học sinh")
                .surveyType(SurveyType.SCREENING)
                .isRequired(true)
                .isRecurring(false)
                .recurringCycle(RecurringCycle.NONE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .categoryId(1)
                .targetScope(TargetScope.ALL)
                .targetGrade(List.of(Grade.GRADE_10, Grade.GRADE_11, Grade.GRADE_12))
                .questions(List.of(
                        createQuestion("Cảm thấy lo lắng, căng thẳng hoặc bất an như thế nào?", 1),
                        createQuestion("Không thể ngừng hoặc kiểm soát sự lo lắng?", 2),
                        createQuestion("Lo lắng quá nhiều về những điều khác nhau?", 3),
                        createQuestion("Gặp khó khăn khi thư giãn?", 4),
                        createQuestion("Bồn chồn đến mức không thể ngồi yên?", 5),
                        createQuestion("Dễ bị khó chịu hoặc cáu gắt?", 6),
                        createQuestion("Sợ rằng điều tồi tệ có thể xảy ra?", 7)
                ))
                .build();
    }

    @Test
    @WithMockUser(username = "counselorUser", roles = {"COUNSELOR"})
    void createSurvey() throws Exception {

        //GIVEN
        String content = objectMapper.writeValueAsString(surveyDto);

        Mockito.when(surveyService.addNewSurvey(Mockito.any(AddNewSurveyDto.class)))
                .thenReturn(1);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/survey")
                        .contentType(MediaType.APPLICATION_JSON_VALUE )
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json("1")); // ✅ Kiểm tra body trả về
        //THEN
    }
}

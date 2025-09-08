package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
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
import java.time.LocalDateTime;
import java.util.List;

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

    private SurveyDetailResponse surveyDetailResponse;

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

    private List<GradeDto> grades = null;

    private List<SurveyGetAllResponse> surveys;

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

            AnswerResponse answer1 = AnswerResponse.builder()
                    .id(1)
                    .text("Không bao giờ")
                    .score(0)
                    .build();

            AnswerResponse answer2 = AnswerResponse.builder()
                    .id(2)
                    .text("Vài ngày")
                    .score(1)
                    .build();

            QuestionResponse question = QuestionResponse.builder()
                    .questionId(1)
                    .text("Bạn cảm thấy lo lắng trong tuần qua?")
                    .description("Câu hỏi về mức độ lo lắng")
                    .questionType("MULTIPLE_CHOICE")
                    .isActive(true)
                    .isRequired(true)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now())
                    .answers(List.of(answer1, answer2))
                    .build();

            CategoryResponse category = CategoryResponse.builder()
                    .id(1)
                    .name("Tâm lý học đường")
                    .description("Các khảo sát liên quan đến tâm lý học sinh")
                    .build();

            grades = List.of(
                    GradeDto.builder().targetLevel(Grade.GRADE_10.name()).build(),
                    GradeDto.builder().targetLevel(Grade.GRADE_11.name()).build(),
                    GradeDto.builder().targetLevel(Grade.GRADE_12.name()).build()
                    );

            surveyDetailResponse = SurveyDetailResponse.builder()
                    .surveyId(1)
                    .title("Khảo sát học sinh")
                    .description("Khảo sát tâm lý học sinh")
                    .isRequired(true)
                    .isRecurring(false)
                    .recurringCycle(RecurringCycle.NONE.toString())
                    .surveyType("SCREENING")
                    .status("DRAFT")
                    .targetScope("ALL")
                    .targetGrade(grades)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(7))
                    .category(category)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .updatedAt(LocalDateTime.now())
                    .questions(List.of(question))
                    .build();



        SurveyGetAllResponse survey1 = SurveyGetAllResponse.builder()
                .surveyId(1)
                .title("Khảo sát tâm lý học sinh")
                .description("Khảo sát định kỳ để kiểm tra tâm lý học sinh cấp 3")
                .isRequired(true)
                .isRecurring(false)
                .recurringCycle(RecurringCycle.NONE.name())
                .surveyType(SurveyType.SCREENING.name())
                .status("DRAFT")
                .targetScope(TargetScope.ALL.name())
                .targetGrade(grades)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .category(category)
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .createdBy(null)
                .build();

        SurveyGetAllResponse survey2 = SurveyGetAllResponse.builder()
                .surveyId(2)
                .title("Khảo sát học lực")
                .description("Đánh giá học lực giữa kỳ")
                .isRequired(false)
                .isRecurring(true)
                .recurringCycle(RecurringCycle.MONTHLY.name())
                .surveyType(SurveyType.SCREENING.name())
                .status("PUBLISHED")
                .targetScope(TargetScope.GRADE.name())
                .targetGrade(List.of(GradeDto.builder().targetLevel(Grade.GRADE_10.name()).build()))
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().plusDays(7))
                .category(category)
                .createdAt(LocalDateTime.now().minusDays(5))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .createdBy(null)
                .build();

        surveys = List.of(survey1, survey2);
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

    @Test
    @WithMockUser(username = "studentUser", roles = {"STUDENT"})
    void getSurveyById() throws Exception {
        //GIVEN
        Integer surveyId = 1;

        Mockito.when(surveyService.getSurveyById(surveyId))
                .thenReturn(surveyDetailResponse);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/survey/{id}", surveyId)
                .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.surveyId").value(surveyId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Khảo sát học sinh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.questions[0].text").value("Bạn cảm thấy lo lắng trong tuần qua?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.questions[0].answers[0].text").value("Không bao giờ"));
        //THEN
    }


    @Test
    @WithMockUser(username = "studentUser", roles = {"STUDENT"})
    void testGetSurveyById_NotFound() throws Exception {
        //GIVEN
        Integer surveyId = 1;

        Mockito.when(surveyService.getSurveyById(surveyId))
                .thenThrow(new RuntimeException("Something went wrong"));
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/survey/{id}", surveyId)
                        .content(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        //THEN
    }

    @Test
    @WithMockUser(username = "managerUser", roles = {"MANAGER"})
    void getAllSurveys() throws Exception {
        Mockito.when(surveyService.getAllSurveys()).thenReturn(surveys);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/survey")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }



}

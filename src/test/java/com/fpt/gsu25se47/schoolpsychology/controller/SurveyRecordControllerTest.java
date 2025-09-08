package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.common.PaginationResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSurveyRecordDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RecurringCycle;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.PaginationUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SurveyRecordControllerTest {

    private static final Logger log = LoggerFactory.getLogger(SurveyControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyRecordService surveyRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaginationUtil paginationUtil;

    private SurveyRecordGetAllResponse surveyRecordResponse;


    private CreateSurveyRecordDto createSurveyRecordDto;

    private SurveyRecordDetailResponse surveyRecordDetailResponse;

    @BeforeEach
    void initDate(){
        SubmitAnswerRecordRequest answer1 = createAnswerRecord(1,1,false);

        SubmitAnswerRecordRequest answer2 = createAnswerRecord(2,2,false);

        createSurveyRecordDto = CreateSurveyRecordDto.builder()
                .isSkipped(false)
                .surveyId(1)
                .totalScore(3.0f)
                .surveyRecordType("SCREENING") // hoặc null nếu không cần
                .answerRecordRequests(List.of(answer1, answer2))
                .build();



        // Khởi tạo thông tin cấp độ (nếu có phân loại theo điểm)
        LevelResponse level = LevelResponse.builder()
                .id(1)
                .label("Bình thường")
                .description("Không có dấu hiệu tâm lý bất thường")
                .build();

        // Tái sử dụng dữ liệu khảo sát từ `SurveyGetAllResponse`
        SurveyGetAllResponse survey = SurveyGetAllResponse.builder()
                .surveyId(1)
                .title("Khảo sát tâm lý học sinh")
                .description("Mô tả khảo sát")
                .isRequired(true)
                .isRecurring(false)
                .recurringCycle(RecurringCycle.NONE.name())
                .surveyType("SCREENING")
                .status("PUBLISHED")
                .targetScope("ALL")
                .targetGrade(List.of(
                        GradeDto.builder().targetLevel("GRADE_10").build(),
                        GradeDto.builder().targetLevel("GRADE_11").build()
                ))
                .startDate(LocalDate.now().minusDays(5))
                .endDate(LocalDate.now().plusDays(5))
                .category(CategoryResponse.builder()
                        .id(1)
                        .name("Tâm lý học đường")
                        .description("Mô tả danh mục")
                        .build())
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now().minusDays(2))
                .createdBy(null)
                .build();

        List<AnswerRecordResponse> list = new ArrayList<>();

        for(int i = 1; i <= 2; i++){
            list.add(answerRecordResponse(1,1, 1, "Ban on khong", "Binh Thuong", i));
        }

        // Gộp tất cả thành response cuối cùng
        surveyRecordDetailResponse = SurveyRecordDetailResponse.builder()
                .id(101)
                .totalScore(2.0f)
                .isSkipped(false)
                .surveyRecordType(SurveyRecordType.SCREENING)
                .level(level)
                .completedAt(LocalDateTime.now())
                .survey(survey)
                .answerRecords(list)
                .build();


        surveyRecordResponse = SurveyRecordGetAllResponse.builder()
                .id(1)
                .totalScore(8.5f)
                .isSkipped(false)
                .identify(null)
                .completedAt(LocalDateTime.now().minusHours(1))
                .level(level)
                .survey(survey)
                .build();
    }

    private SubmitAnswerRecordRequest createAnswerRecord(int questionId, int answerId, boolean isSkipped) {
        return SubmitAnswerRecordRequest.builder()
                .questionId(questionId)
                .answerId(answerId)
                .isSkipped(isSkipped)
                .build();
    }

    private AnswerRecordResponse answerRecordResponse(Integer questionId, Integer answerId, int score, String textQuestion, String textAnswer, Integer count){
        // Khởi tạo câu hỏi
        QuestionDto questionDto = QuestionDto.builder()
                .questionId(questionId)
                .text(textQuestion)
                .build();

        AnswerResponse answerResponse = AnswerResponse.builder()
                .id(answerId)
                .text(textAnswer)
                .score(score)
                .build();

        AnswerRecordDto answerDto = AnswerRecordDto.builder()
                .answerResponse(answerResponse)
                .questionResponse(questionDto)
                .build();


        // Gắn kết câu trả lời vào record
        AnswerRecordResponse answerRecord = AnswerRecordResponse.builder()
                .id(count)
                .questionResponse(null)
                .answerResponse(answerDto)
                .skipped(false)
                .build();

        return answerRecord;
    }

    @Test
    @WithMockUser(username = "studentUser", roles = {"STUDENT"})
    void createSurveyRecord() throws Exception {
        //GIVEN
        Mockito.when(surveyRecordService.createSurveyRecord(Mockito.any(CreateSurveyRecordDto.class), Mockito.isNull()))
                .thenReturn(surveyRecordDetailResponse);
        //WHEN
        mockMvc.perform(post("/api/v1/survey-records")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createSurveyRecordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalScore").value(2.0))
                .andExpect(jsonPath("$.survey.title").value("Khảo sát tâm lý học sinh"));
//                .andExpect(jsonPath("$.answerRecords[0].answerResponse.text").value("Vài ngày"));
        //THEN
    }

    @Test
    @WithMockUser(username = "STUDENT", roles = {"STUDENT"})
    void getAllSurveyRecord() throws Exception {
        // GIVEN
        int accountId = 123;
        int page = 0;
        int size = 5;
        String field = "completedAt";
        String direction = "desc";

        PageRequest pageRequest = PageRequest.of(page, size);
        List<SurveyRecordGetAllResponse> content = List.of(surveyRecordResponse);
        Page<SurveyRecordGetAllResponse> mockPage = new PageImpl<>(content, pageRequest, 1);

//        PaginationResponse response = paginationUtil.getPaginationResponse(0, pageRequest, mockPage, content);

//        return ResponseEntity.ok(paginationUtil.getPaginationResponse(count, pageRequest, surveyRecordResponses, surveyRecordResponses.getContent()));


        PaginationResponse response = PaginationResponse.builder()
                .page(0)
                .size(5)
                .totalPages(1)
                .numberOfSkipped(0)
                .totalElements(1) // <-- giá trị bạn muốn test
                .hasNext(false)
                .hasPrevious(false)
                .content(List.of(surveyRecordResponse))
                .build();

        // MOCK
        Mockito.when(paginationUtil.getPageRequest(page, size, direction, field)).thenReturn(pageRequest);
        Mockito.when(surveyRecordService.getAllSurveyRecordById(null, accountId, pageRequest)).thenReturn(mockPage);
        Mockito.when(surveyRecordService.countSurveyRecordSkippedByAccountId(accountId)).thenReturn(0);
        Mockito.when(paginationUtil.getPaginationResponse(
                Mockito.eq(0),
                Mockito.eq(pageRequest),
                Mockito.eq(mockPage),
                Mockito.anyList()
        )).thenReturn(response);

        // WHEN + THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/survey-records/accounts/{accountId}", accountId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("direction", direction)
                        .param("field", field)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].totalScore").value(8.5f))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].survey.title").value("Khảo sát tâm lý học sinh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].level.label").value("Bình thường"));
    }


}

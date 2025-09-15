package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAnswerDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewQuestionDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewSurveyDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.AccountMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class SupportProgramControllerTest {

    private static final Logger log = LoggerFactory.getLogger(SupportProgramControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountMapper accountMapper;

    @MockBean
    private SupportProgramService supportProgramService;

    private AddNewSurveyDto surveyDto;

    private SupportProgramRequest supportProgramRequest;

    private SupportProgramResponse supportProgramResponse;

    private SupportProgramDetail supportProgramDetail;

    private MultipartFile thumbnail;

    private ProgramParticipantsResponse participant1;

    private RegisterProgramParticipantResponse registerProgramParticipantResponse;

    private final String BASE_URL = "/api/v1/support-programs";

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
    void initData() {
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



        Account studentAccount = Account.builder()
                .id(1001)
                .email("student1@example.com")
                .password("{noop}password123") // giả sử dùng NoOpPasswordEncoder
                .role(Role.STUDENT)
                .status(true)
                .phoneNumber("0901234567")
                .fullName("Nguyen Van A")
                .gender(true) // Nam
                .dob(LocalDate.of(2005, 5, 20))
                .build();

        Student student = Student.builder()
                .id(3001)
                .studentCode("STU1001")
                .account(studentAccount)
                .build();

        StudentDto studentDto = StudentDto.builder()
                .studentCode(student.getStudentCode())
                .isEnableSurvey(true)
                .targetLevel(null)
                .caseId(null)
                .classDto(null)
                .build();

        studentAccount.setStudent(student);

        Account counselorAccount = Account.builder()
                .id(2001)
                .email("counselor1@example.com")
                .password("{noop}password123")
                .role(Role.COUNSELOR)
                .status(true)
                .phoneNumber("0912345678")
                .fullName("Tran Thi B")
                .gender(false) // Nữ
                .dob(LocalDate.of(1990, 8, 15))
                .build();

        Counselor counselor = Counselor.builder()
                .id(4001)
                .counselorCode("CL001")
                .account(counselorAccount)
                .build();
        counselorAccount.setCounselor(counselor);

        participant1 = ProgramParticipantsResponse.builder()
                .id(1)
                .student(accountMapper.toDto(studentAccount))
                .cases(null)
                .joinAt(LocalDateTime.now().minusDays(2))
                .status(RegistrationStatus.ENROLLED)
                .finalScore(85.5f)
                .surveyIn(70.0f)
                .surveyOut(90.0f)
                .build();


        CategoryResponse category = CategoryResponse.builder()
                .id(1)
                .name("Tâm lý học đường")
                .description("Chương trình hỗ trợ liên quan đến tâm lý học sinh")
                .build();

        // Survey (nếu có)
        SurveyGetAllResponse survey = SurveyGetAllResponse.builder()
                .surveyId(10)
                .title("Khảo sát mức độ lo âu")
                .description("Khảo sát ban đầu để đánh giá lo âu của học sinh")
                .surveyType("SCREENING")
                .status("PUBLISHED")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        // Support Program Detail
        supportProgramDetail = SupportProgramDetail.builder()
                .id(1)
                .name("Chương trình tư vấn tâm lý học đường")
                .description("Chương trình giúp học sinh giảm stress, lo âu")
                .maxParticipants(50)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(10))
                .status(ProgramStatus.ACTIVE)
                .isActiveSurvey(true)
                .thumbnail(Map.of("thumbnail.jpg", "sop20033"))
                .location("Phòng hội thảo A1")
                .category(category)
                .hostedBy(accountMapper.toDto(counselorAccount))
                .programSurvey(survey)
                .participants(List.of(participant1))
                .build();

        //Mock request
        supportProgramRequest = SupportProgramRequest.builder()
                .name("Chương trình tư vấn tâm lý")
                .description("Hỗ trợ học sinh vượt qua căng thẳng, lo âu trong học tập")
                .maxParticipants(50)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .location("Phòng hội thảo A1")
                .hostedBy(1001)
                .addNewSurveyDto(surveyDto)
                .build();

        // Mock response
        supportProgramResponse = SupportProgramResponse.builder()
                .id(1)
                .name("Chương trình tư vấn tâm lý")
                .description("Hỗ trợ học sinh vượt qua căng thẳng, lo âu trong học tập")
                .maxParticipants(50)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(2))
                .createdDate(LocalDateTime.now().minusDays(1))
                .updatedDate(LocalDateTime.now())
                .status(ProgramStatus.ACTIVE)
                .isActiveSurvey(true)
                .thumbnail(Map.of("thumbnail.jpg", "sop20033"))
                .location("Phòng hội thảo A1")
                .participants(0)
                .build();

        // Fake MultipartFile
        thumbnail = new MockMultipartFile(
                "thumbnail",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );


        //Register Support Program
        registerProgramParticipantResponse = new RegisterProgramParticipantResponse();
        registerProgramParticipantResponse.setId(1);
        registerProgramParticipantResponse.setSupportProgram(supportProgramResponse);
        registerProgramParticipantResponse.setStudent(studentDto);
        registerProgramParticipantResponse.setJoinAt(LocalDateTime.now());
        registerProgramParticipantResponse.setStatus(RegistrationStatus.ENROLLED);
    }


    @Test
    @WithMockUser(username = "managerUser", roles = {"MANAGER"})
    void createSupportProgram() throws Exception {
        // GIVEN
        Mockito.when(supportProgramService.createSupportProgram(Mockito.any(MultipartFile.class), Mockito.any(SupportProgramRequest.class)))
                .thenReturn(supportProgramResponse);

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL)
                        .file((MockMultipartFile) thumbnail) // gửi file thumbnail
                        .file(new MockMultipartFile(
                                "request",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsBytes(supportProgramRequest) // gửi JSON request
                        ))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(supportProgramResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(supportProgramResponse.getName()));
    }

    @Test
    @WithMockUser(username = "counselorUser", roles = {"COUNSELOR"})
    void getAllSupportProgram() throws Exception{
        //GIVEN
        Mockito.when(supportProgramService.getAllSupportPrograms())
                .thenReturn(List.of(supportProgramResponse));
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1));
        //THEN
    }

    @Test
    @WithMockUser(username = "counselorUser", roles = {"COUNSELOR"})
    void getSupportProgramById() throws Exception{
        //GIVEN
        Integer supportProgramId = 1;

        Mockito.when(supportProgramService.getSupportProgramById(supportProgramId))
                .thenReturn(supportProgramDetail);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + supportProgramId)  // ✅ sửa ở đây
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(supportProgramDetail.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.participants.length()").value(1));
        //THEN
    }


    @Test
    @WithMockUser(username = "studentUser", roles = {"STUDENT"})
    void registerSupportProgram() throws Exception{
        //GIVEN
        Integer supportProgramId = 1;
        Mockito.when(supportProgramService.registerStudentToSupportProgram(supportProgramId))
                .thenReturn(registerProgramParticipantResponse);
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/participants/register")
                .param("programId", supportProgramId.toString())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.student.studentCode").value("STU1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(RegistrationStatus.ENROLLED.name()));
        //THEN
    }

    @Test
    @WithMockUser(username = "counselorUser", roles = {"COUNSELOR"})
    void addStudentIntoSupportProgramByCaseId() throws Exception{
        //GIVEN
        Integer programId = 1;
        List<Integer> caseIds = List.of(1);
        Mockito.when(supportProgramService.addParticipantsToSupportProgram(programId, caseIds))
                .thenReturn(List.of(participant1));
        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/add-participants")
                .param("programId", programId.toString())
                .param("listCaseIds", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].student.email").value("student1@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(RegistrationStatus.ENROLLED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].finalScore").value(85.5f))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surveyIn").value(70.0f))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surveyOut").value(90.0f));

        //THEN
    }
}

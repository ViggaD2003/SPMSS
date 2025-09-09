package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAssessmentScoreRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AssessmentScoreResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAppointmentRequest appointmentRequest;
    private AppointmentResponse appointmentResponse;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void initData() {
        startDateTime = LocalDateTime.now().plusDays(1).withHour(16).withMinute(0);
        endDateTime = LocalDateTime.now().plusDays(1).withHour(16).withMinute(30);

        appointmentRequest = new CreateAppointmentRequest();
        appointmentRequest.setStartDateTime(startDateTime);
        appointmentRequest.setEndDateTime(endDateTime);
        appointmentRequest.setSlotId(1);
        appointmentRequest.setCaseId(null);
        appointmentRequest.setHostType(HostType.COUNSELOR);
        appointmentRequest.setIsOnline(true);
        appointmentRequest.setReasonBooking("I am feeling sad");
        appointmentRequest.setBookedForId(2);

        AccountDto bookedForAccount = new CounselorDto();
        bookedForAccount.setId(2);
        bookedForAccount.setFullName("John Doe");

        AccountDto bookedByAccount = new StudentDto();
        bookedByAccount.setId(3);
        bookedByAccount.setFullName("Jane Smith");

        AccountDto hostedByAccount = new CounselorDto();
        hostedByAccount.setId(4);
        hostedByAccount.setFullName("Dr. Wilson");

        appointmentResponse = AppointmentResponse.builder()
                .id(1)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .bookedFor(bookedForAccount)
                .bookedBy(bookedByAccount)
                .hostedBy(hostedByAccount)
                .hostType(HostType.COUNSELOR)
                .isOnline(true)
                .location("Online Session")
                .cancelReason(null)
                .assessmentScores(null)
                .noteSummary(null)
                .noteSuggestion(null)
                .studentCoopLevel(null)
                .status(AppointmentStatus.CONFIRMED)
                .sessionFlow(null)
                .reasonBooking("I am feeling sad")
                .build();
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void createAppointment_validRequest_success() throws Exception {

        // GIVEN
        String content = objectMapper.writeValueAsString(appointmentRequest);

        Mockito.when(appointmentService.createAppointment(Mockito.any(CreateAppointmentRequest.class)))
                .thenReturn(appointmentResponse);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookedFor.id").value(2))
                .andExpect(jsonPath("$.bookedFor.fullName").value("John Doe"))
                .andExpect(jsonPath("$.bookedBy.id").value(3))
                .andExpect(jsonPath("$.bookedBy.fullName").value("Jane Smith"))
                .andExpect(jsonPath("$.hostedBy.id").value(4))
                .andExpect(jsonPath("$.hostedBy.fullName").value("Dr. Wilson"))
                .andExpect(jsonPath("$.hostType").value("COUNSELOR"))
                .andExpect(jsonPath("$.isOnline").value(true))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void createAppointment_pastStartDateTime_badRequest() throws Exception {
        // GIVEN - past date times
        LocalDateTime pastStartTime = LocalDateTime.now().minusHours(1);

        appointmentRequest.setStartDateTime(pastStartTime);

        String content = objectMapper.writeValueAsString(appointmentRequest);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.startDateTime").value("Start date/time must be in the present or future")); // Assuming validation errors are returned
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void createAppointment_pastEndDateTime_badRequest() throws Exception {
        // GIVEN - past date times
        LocalDateTime pastEndTime = LocalDateTime.now().minusHours(1);

        appointmentRequest.setEndDateTime(pastEndTime);

        String content = objectMapper.writeValueAsString(appointmentRequest);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.endDateTime").value("End date/time must be in the future")); // Assuming validation errors are returned
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void cancelAppointment_validRequest_success() throws Exception {
        //GIVEN
        Integer id = appointmentResponse.getId();
        String cancelReason = "I have another appointment";

        appointmentResponse.setCancelReason(cancelReason);
        appointmentResponse.setStatus(AppointmentStatus.CANCELED);

        Mockito.when(appointmentService.cancelAppointment(id, cancelReason))
                .thenReturn(appointmentResponse);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/appointments/cancel/{id}", id)
                        .param("reasonCancel", cancelReason)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value(AppointmentStatus.CANCELED.name()));
    }

    @Test
    @WithMockUser(username = "studentUser", roles = "STUDENT")
    void cancelAppointment_missingReasonCancel_returnsInternalError() throws Exception {
        // GIVEN
        Integer id = 1;

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/appointments/cancel/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        // Service should NOT be called
        Mockito.verify(appointmentService, Mockito.never())
                .cancelAppointment(Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    @WithMockUser(username = "teacherUser", roles = "TEACHER")
    void updateAppointment_asTeacher_success() throws Exception {
        // GIVEN
        Integer id = 1;
        UpdateAppointmentRequest request = new UpdateAppointmentRequest();
        request.setSessionNotes("Updated by teacher");
        request.setNoteSummary("Updated by teacher");
        request.setNoteSuggestion("Updated by teacher");
        request.setCaseId(1);
        request.setStudentCoopLevel(StudentCoopLevel.GOOD);
        request.setSessionFlow(SessionFlow.AVERAGE);
        // Teacher sends assessmentScores but they should be ignored
        CreateAssessmentScoreRequest scoreRequest = new CreateAssessmentScoreRequest();
        scoreRequest.setChronicityScore(3.0f);
        scoreRequest.setFrequencyScore(1.0f);
        scoreRequest.setSeverityScore(2.0f);
        scoreRequest.setImpairmentScore(1.0f);

        request.setAssessmentScores(List.of(scoreRequest));

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .noteSuggestion(request.getNoteSuggestion())
                .noteSummary(request.getNoteSummary())
                .sessionNotes(request.getSessionNotes())
                .assessmentScores(List.of(getAssessmentScore()))
                .build();

        Mockito.when(appointmentService.updateAppointment(ArgumentMatchers.eq(id), ArgumentMatchers.any(UpdateAppointmentRequest.class)))
                .thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/appointments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.noteSuggestion").value("Updated by teacher"))
                .andExpect(jsonPath("$.assessmentScores").exists())
                .andExpect(jsonPath("$.assessmentScores").isArray())
                .andExpect(jsonPath("$.assessmentScores[0].categoryId").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].severityScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].frequencyScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].impairmentScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].chronicityScore").value(1));

        Mockito.verify(appointmentService).updateAppointment(ArgumentMatchers.eq(id), ArgumentMatchers.any(UpdateAppointmentRequest.class));
    }

    @Test
    @WithMockUser(username = "counselorUser", roles = "COUNSELOR")
    void updateAppointment_asCounselor_success() throws Exception {
        // GIVEN
        Integer id = 1;
        UpdateAppointmentRequest request = getUpdateAppointmentRequest();


        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .noteSuggestion(request.getNoteSuggestion())
                .noteSummary(request.getNoteSummary())
                .sessionNotes(request.getSessionNotes())
                .assessmentScores(List.of(getAssessmentScore()))
                .build();

        Mockito.when(appointmentService.updateAppointment(ArgumentMatchers.eq(id), ArgumentMatchers.any(UpdateAppointmentRequest.class)))
                .thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/appointments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.noteSuggestion").value("Updated by counselor"))
                .andExpect(jsonPath("$.assessmentScores").isArray())
                .andExpect(jsonPath("$.assessmentScores[0].categoryId").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].severityScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].frequencyScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].impairmentScore").value(1))
                .andExpect(jsonPath("$.assessmentScores[0].chronicityScore").value(1));

        Mockito.verify(appointmentService).updateAppointment(ArgumentMatchers.eq(id), ArgumentMatchers.any(UpdateAppointmentRequest.class));
    }

    private static AssessmentScoreResponse getAssessmentScore() {
        AssessmentScoreResponse assessmentScoreResponse = new AssessmentScoreResponse();
        assessmentScoreResponse.setCategoryId(1);
        assessmentScoreResponse.setChronicityScore(1.0f);
        assessmentScoreResponse.setFrequencyScore(1.0f);
        assessmentScoreResponse.setSeverityScore(1.0f);
        assessmentScoreResponse.setImpairmentScore(1.0f);

        return assessmentScoreResponse;
    }

    private static UpdateAppointmentRequest getUpdateAppointmentRequest() {
        UpdateAppointmentRequest request = new UpdateAppointmentRequest();
        request.setSessionNotes("Updated by counselor");
        request.setNoteSummary("Updated by counselor");
        request.setNoteSuggestion("Updated by counselor");
        request.setCaseId(1);
        request.setStudentCoopLevel(StudentCoopLevel.GOOD);
        request.setSessionFlow(SessionFlow.AVERAGE);

        CreateAssessmentScoreRequest scoreRequest = new CreateAssessmentScoreRequest();
        scoreRequest.setCategoryId(1);
        scoreRequest.setChronicityScore(1.0f);
        scoreRequest.setFrequencyScore(1.0f);
        scoreRequest.setSeverityScore(1.0f);
        scoreRequest.setImpairmentScore(1.0f);

        request.setAssessmentScores(List.of(scoreRequest));
        return request;
    }

}

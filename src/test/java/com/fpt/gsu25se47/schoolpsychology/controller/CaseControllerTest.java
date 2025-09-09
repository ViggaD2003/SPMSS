package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateCaseRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.LevelResponse;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Priority;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgressTrend;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CaseService caseService;

    @Autowired
    private ObjectMapper objectMapper;
    private AddNewCaseDto addNewCaseDto;
    private String createCaseResponse;

    private static final String baseURL = "/api/v1/cases";

    @BeforeEach
    void initData() {
        addNewCaseDto = AddNewCaseDto.builder()
                .title("Case for test")
                .description("Case for test")
                .currentLevelId(1)
                .initialLevelId(1)
                .studentId(2)
                .priority(Priority.HIGH)
                .progressTrend(ProgressTrend.STABLE)
                .createBy(1)
                .build();

        createCaseResponse = "Case created!";
    }

    @Test
    @WithMockUser(username = "teacherUser", roles = "TEACHER")
    void createCase_validRequest_success() throws Exception {
        // GIVEN
        String content = objectMapper.writeValueAsString(addNewCaseDto);

        Mockito.when(caseService.createCase(Mockito.any(AddNewCaseDto.class)))
                .thenReturn((Optional) Optional.of(createCaseResponse));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/cases")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(content().string("\"" + createCaseResponse + "\""));
    }

    @Test
    @WithMockUser(username = "teacherUser", roles = "TEACHER")
    void createCase_invalidRequest_shouldReturnBadRequest() throws Exception {
        AddNewCaseDto invalidDto = AddNewCaseDto.builder()
                .title("") // invalid because @NotBlank
                .description("A".repeat(1200)) // invalid because > 1000 chars
                .priority(null) // invalid because @NotNull
                .progressTrend(null)
                .studentId(-1) // invalid because @Positive
                .createBy(0)   // invalid because must be positive
                .currentLevelId(null)
                .initialLevelId(null)
                .build();

        String content = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post(baseURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.studentId").value("Student ID must be a positive number"))
                .andExpect(jsonPath("$.createBy").value("Created by must be a positive number"))
                .andExpect(jsonPath("$.title").value("Title must not be blank"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void assignCaseToCounselor_validRequest_success() throws Exception {
        Integer counselorId = 10;
        List<Integer> caseIds = List.of(1, 2, 3);

        Mockito.when(caseService.assignCounselor(counselorId, caseIds))
                .thenReturn((Optional) Optional.of("Case assigned successfully !"));

        mockMvc.perform(patch(baseURL + "/assign")
                        .param("counselorId", counselorId.toString())
                        .param("caseId", "1", "2", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Case assigned successfully !\""));
    }

    @Test
    @WithMockUser(roles = "COUNSELOR")
    void addSurveyToCases_validRequest_success() throws Exception {
        List<Integer> caseIds = List.of(1, 2);
        Integer surveyId = 99;

        Mockito.when(caseService.addSurveyCaseLink(caseIds, surveyId))
                .thenReturn((Optional) Optional.of("Added survey case link successfully !"));

        mockMvc.perform(post(baseURL + "/add-survey")
                        .param("caseIds", "1", "2")
                        .param("surveyId", surveyId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Added survey case link successfully !\""));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeSurveyFromCases_bulk_success() throws Exception {
        List<Integer> caseIds = List.of(1, 2);

        Mockito.when(caseService.removeSurveyCaseLink(caseIds))
                .thenReturn((Optional) Optional.of("Removed survey case link successfully !"));

        mockMvc.perform(patch(baseURL + "/remove")
                        .param("caseIds", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Removed survey case link successfully !\""));
    }

    @Test
    @WithMockUser
    void removeSurveyByCaseId_success() throws Exception {
        List<Integer> caseIds = List.of(1, 2);
        Integer surveyId = 55;

        Mockito.when(caseService.removeSurveyByCaseId(caseIds, surveyId))
                .thenReturn((Optional) Optional.of("Removed survey case link successfully !"));

        mockMvc.perform(patch( baseURL + "/remove-survey")
                        .param("caseIds", "1", "2")
                        .param("surveyId", surveyId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("\"Removed survey case link successfully !\""));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getCaseById_validId_success() throws Exception {
        Integer caseId = 1;
        CaseGetAllResponse response = CaseGetAllResponse.builder()
                .id(caseId)
                .title("Sample Case")
                .build();

        Mockito.when(caseService.getDetailById(caseId))
                .thenReturn((Optional) Optional.of(response));

        mockMvc.perform(get(baseURL + "/" + caseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(caseId))
                .andExpect(jsonPath("$.title").value("Sample Case"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getCaseById_caseNotFound_notFound() throws Exception {
        Integer caseId = 99;

        Mockito.when(caseService.getDetailById(caseId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Case not found"));

        mockMvc.perform(get(baseURL + "/" + caseId))
                .andExpect(status().isNotFound());
    }

    // 6. Update case
    @Test
    @WithMockUser(roles = "COUNSELOR")
    void updateCase_validRequest_success() throws Exception {
        Integer caseId = 1;
        UpdateCaseRequest updateRequest = UpdateCaseRequest
                .builder()
                .progressTrend(ProgressTrend.STABLE)
                .priority(Priority.HIGH)
                .status(Status.REJECTED)
                .currentLevelId(2)
                .build();

        CaseGetAllResponse response = CaseGetAllResponse.builder()
                .id(caseId)
                .progressTrend(ProgressTrend.STABLE)
                .priority(Priority.HIGH)
                .status(Status.REJECTED)
                .currentLevel(LevelResponse.builder()
                        .id(2)
                        .build())
                .build();

        Mockito.when(caseService.updateCase(Mockito.eq(caseId), Mockito.any(UpdateCaseRequest.class)))
                .thenReturn(Optional.of(response));

        String json = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put(baseURL + "/" + caseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(caseId))
                .andExpect(jsonPath("$.status").value(Status.REJECTED.name()))
                .andExpect(jsonPath("$.progressTrend").value(ProgressTrend.STABLE.name()))
                .andExpect(jsonPath("$.priority").value(Priority.HIGH.name()))
                .andExpect(jsonPath("$.currentLevel.id").value(2));
    }

    @Test
    @WithMockUser(roles = "COUNSELOR")
    void updateCase_invalidRequest_badRequest() throws Exception {
        Integer caseId = 1;
        UpdateCaseRequest updateRequest = UpdateCaseRequest
                .builder()
                .build();

        CaseGetAllResponse response = CaseGetAllResponse.builder()
                .id(caseId)
                .progressTrend(ProgressTrend.STABLE)
                .priority(Priority.HIGH)
                .status(Status.REJECTED)
                .currentLevel(LevelResponse.builder()
                        .id(2)
                        .build())
                .build();

        Mockito.when(caseService.updateCase(Mockito.eq(caseId), Mockito.any(UpdateCaseRequest.class)))
                .thenReturn(Optional.of(response));

        String json = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put(baseURL + "/" + caseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.currentLevelId").value("CurrentLevelId không được để trống"))
                .andExpect(jsonPath("$.progressTrend").value("ProgressTrend không được để trống"))
                .andExpect(jsonPath("$.priority").value("Priority không được để trống"))
                .andExpect(jsonPath("$.status").value("Status không được để trống"));
    }
}

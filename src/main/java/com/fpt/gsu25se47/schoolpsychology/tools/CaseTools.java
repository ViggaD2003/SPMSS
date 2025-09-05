package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CaseTools {

    private final CaseService caseService;

    @Tool(description = """
    Get all psychology cases with optional filtering by status, category, survey, or account
    The accountId is taken from the system prompt where you can find the accountId of the current account log in, and also the role of this account, and this accountId is required
    """)
    public List<CaseGetAllResponse> getAllCases(List<String> statusCase, Integer categoryId, Integer surveyId, Integer accountId) {
        return caseService.getAllCases(statusCase, categoryId, surveyId, accountId);
    }

    @Tool(description = "Get detailed information for a specific case by ID")
    public Optional<?> getCaseDetailById(Integer caseId) {
        return caseService.getDetailById(caseId);
    }
}

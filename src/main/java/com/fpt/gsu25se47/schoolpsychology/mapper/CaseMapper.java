package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationStatic;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseMapper {

    private final LevelMapper levelMapper;
    private final AccountMapper accountMapper;


    public CaseGetAllResponse mapToCaseGetAllResponse(Cases cases, Integer surveyId) {
        return CaseGetAllResponse.builder()
                .id(cases.getId())
                .title(cases.getTitle())
                .description(cases.getDescription())
                .priority(cases.getPriority())
                .status(cases.getStatus())
                .progressTrend(cases.getProgressTrend())
                .createBy(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCreateBy()))
                .isAddSurvey(surveyId == null ? null : cases.getSurveyCaseLinks().stream().anyMatch(item -> item.getSurvey().getId().equals(surveyId)))
                .counselor(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCounselor()))

                .student(cases.getStudent() == null ? null : accountMapper.toDto(cases.getStudent()))

                .initialLevel(levelMapper.mapToLevelResponse(cases.getInitialLevel()))
                .currentLevel(levelMapper.mapToLevelResponse(cases.getCurrentLevel()))

                .build();
    }

    public CaseGetDetailResponse mapCaseGetDetailResponse(Cases cases, MentalEvaluationStatic statics) {
        return CaseGetDetailResponse.builder()
                .caseInfo(mapToCaseGetAllResponse(cases, null))
                .groupedStatic(statics)
                .build();
    }
}

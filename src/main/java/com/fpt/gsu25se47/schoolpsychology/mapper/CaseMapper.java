package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.InfoAccount;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentCaseDto;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseMapper {

    private final LevelMapper levelMapper;

    public CaseGetAllResponse mapToCaseGetAllResponse(Cases cases) {
        return CaseGetAllResponse.builder()
                .id(cases.getId())
                .title(cases.getTitle())
                .description(cases.getDescription())
                .priority(cases.getPriority())
                .status(cases.getStatus())
                .progressTrend(cases.getProgressTrend())

                .createBy(cases.getCreateBy() != null ? InfoAccount.builder()
                        .id(cases.getCreateBy().getId())
                        .fullName(cases.getCreateBy().getFullName())
                        .codeStaff(cases.getCreateBy().getTeacher().getTeacherCode())
                        .build() : null)

                .counselor(cases.getCounselor() != null ? InfoAccount.builder()
                        .id(cases.getCounselor().getId())
                        .fullName(cases.getCounselor().getFullName())
                        .codeStaff(cases.getCounselor().getCounselor().getCounselorCode())
                        .build() : null)

                .student(cases.getStudent() != null ? StudentCaseDto.builder()
                        .id(cases.getStudent().getId())
                        .studentCode(cases.getStudent().getStudent().getStudentCode())
                        .fullName(cases.getStudent().getFullName())
                        .dob(cases.getStudent().getDob())
                        .gender(cases.getStudent().getGender())
                        .build() : null)

                .initialLevel(levelMapper.mapToLevelResponse(cases.getInitialLevel()))
                .currentLevel(levelMapper.mapToLevelResponse(cases.getCurrentLevel()))

                .build();
    }
}

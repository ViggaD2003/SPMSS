package com.fpt.gsu25se47.schoolpsychology.mapper.Dashboard.Teacher;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.CaseSummDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.CounselorMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.StudentMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, CounselorMapper.class})
public interface CaseSummDetailMapper {

    @Mapping(target = "caseId", source = "id")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "counselorDto", source = "counselor.counselor")
    @Mapping(target = "studentDto", source = "student.student")
    @Mapping(target = "initialLevel", source = "currentLevel.label")
    @Mapping(target = "currentLevel", source = "currentLevel.label")
    @Mapping(target = "lastUpdated", source = "updatedDate")
    @Mapping(target = "categoryName", source = "currentLevel.category.name")
    CaseSummDetailResponse toCaseSummDetailResponse(Cases cases);
}

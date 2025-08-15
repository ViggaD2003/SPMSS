package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentBaseResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentClaimDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = ClassMapper.class)
public interface StudentMapper {

    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "mentalEvaluations", ignore = true)
    @Mapping(target = "classDto", ignore = true) // Tạm ignore để tự xử lý sau
    StudentDto mapStudentDto(Student student, @Context Classes classes);

    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "mentalEvaluations", ignore = true)
    @Mapping(target = "classDto", ignore = true)
    StudentDto mapStudentDtoWithoutClass(Student student);

    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "latestSurveyRecord", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "dob", source = "account.dob")
    StudentSRCResponse toStudentSrcResponse(Student student, @Context List<Integer> caseIds);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "dob", expression = "java(student.getAccount().getDob() != null ? student.getAccount().getDob().toString() : null)")
    StudentClaimDto toStudentClaimDto(Student student);

    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    StudentDetailResponse toStudentDetailResponse(Student student,
                                                  @Context List<CaseGetAllForStudentResponse> cases,
                                                  @Context List<ParentBaseResponse> parentBaseResponses);

    // Hàm xử lý default để map classDto thủ công
    default StudentDto mapStudentDtoWithClass(Student student, Classes classes, ClassMapper classDtoMapper) {
        StudentDto dto = mapStudentDto(student, classes);
        dto.setClassDto(classDtoMapper.toDto(classes));
        return dto;
    }

    @AfterMapping
    default void setHasActiveCasesToStudentSRCResponse(@MappingTarget StudentSRCResponse studentSRCResponse,
                                                       @Context List<Integer> caseIds) {
        studentSRCResponse.setCaseIds(caseIds);
    }

    @AfterMapping
    default void setToStudentDetailResponse(@MappingTarget StudentDetailResponse res,
                                            @Context List<CaseGetAllForStudentResponse> cases,
                                            @Context List<ParentBaseResponse> parentBaseResponses) {
        res.setCases(cases);
        res.setParents(parentBaseResponses);
    }
}
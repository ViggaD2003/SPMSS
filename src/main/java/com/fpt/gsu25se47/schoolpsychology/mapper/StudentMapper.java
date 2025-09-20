package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentBaseResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class StudentMapper {

    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private SchoolYearMapper schoolYearMapper;


    @Named("mapStudentDto")
    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "caseId", ignore = true)
    public abstract StudentDto mapStudentDto(Student student);

    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "dob", source = "account.dob")
    public abstract StudentDto mapStudentDtoWithoutClass(Student student);

    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "latestSurveyRecord", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "dob", source = "account.dob")
    public abstract StudentSRCResponse toStudentSrcResponse(Student student, @Context Integer caseId);

    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    public abstract StudentDetailResponse toStudentDetailResponse(Student student,
                                                                  @Context List<CaseGetAllForStudentResponse> caseResponses,
                                                                  @Context List<ParentBaseResponse> parentBaseResponses);

    public StudentDto mapStudentDtoWithClass(Student student) {
        StudentDto dto = mapStudentDto(student);

        Classes activeClass = classRepository.findActiveClassByStudentId(student.getId());
        dto.setClassDto(classMapper.toDto(activeClass, schoolYearMapper));
        Cases activeCase = caseRepository.findActiveCaseByStudentId(student.getId());
        dto.setCaseId(activeCase == null ? null : activeCase.getId());
        dto.setCaseProfile(activeCase == null ? null : CaseProfileDto.builder()
                        .id(activeCase.getId())
                        .status(activeCase.getStatus())
                        .hostBy(activeCase.getCounselor().getId())
                .build());
        dto.setStatus(student.getAccount().getStatus());
        return dto;
    }

    @AfterMapping
    public void setHasActiveCasesToStudentSRCResponse(@MappingTarget StudentSRCResponse studentSRCResponse,
                                                      @Context Integer caseId) {
        studentSRCResponse.setCaseId(caseId);
    }

    @AfterMapping
    public void setToStudentDetailResponse(@MappingTarget StudentDetailResponse res,
                                           @Context List<CaseGetAllForStudentResponse> caseResponses,
                                           @Context List<ParentBaseResponse> parentBaseResponses) {
        res.setCases(caseResponses);
        res.setParents(parentBaseResponses);
    }
}

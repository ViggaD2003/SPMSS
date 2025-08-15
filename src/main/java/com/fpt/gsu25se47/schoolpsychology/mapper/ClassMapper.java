package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Term.TermResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.ClassesTerm;
import com.fpt.gsu25se47.schoolpsychology.model.Term;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class, SchoolYearMapper.class, TermMapper.class})
public interface ClassMapper {

    @Mapping(target = "isActive", source = "active")
    Classes toClassEntity(CreateClassRequest request);

    @Mapping(target = "schoolYear", expression = "java(mapSchoolYearFromClassTerms(classes.getClassesTerm(), schoolYearMapper))")
    @Mapping(target = "terms", expression = "java(mapClassTermsToTermResponses(classes.getClassesTerm(), termMapper))")
    @Mapping(target = "totalStudents", expression = "java(classes.getEnrollments() != null ? classes.getEnrollments().size() : 0)")
    ClassResponse toClassResponse(Classes classes,
                                  @Context TermMapper termMapper,
                                  @Context SchoolYearMapper schoolYearMapper);

    @Mapping(target = "codeClass", source = "codeClass")
    @Mapping(target = "terms", expression = "java(mapClassTermsToTermResponses(classes.getClassesTerm(), termMapper))")
    @Mapping(target = "schoolYear", expression = "java(mapSchoolYearFromClassTerms(classes.getClassesTerm(), schoolYearMapper))")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassResponseSRC toClassDetailResponseSRC(Classes classes,
                                              @Context TermMapper termMapper,
                                              @Context SchoolYearMapper schoolYearMapper,
                                              @Context List<StudentSRCResponse> students);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    ClassDto toDto(Classes classes);

    void updateClassFromRequest(@MappingTarget Classes classes, UpdateClassRequest request);

    @AfterMapping
    default void setStudentsToClassDetailResponseSRC(@MappingTarget ClassResponseSRC classResponseSRC,
                                                  @Context List<StudentSRCResponse> students) {
        classResponseSRC.setStudents(students);
    }

    default List<TermResponse> mapClassTermsToTermResponses(List<ClassesTerm> classTerms,
                                                            @Context TermMapper termMapper) {
        if (classTerms == null) return null;
        return classTerms.stream()
                .map(ClassesTerm::getTerm)
                .map(termMapper::toTermResponse)
                .collect(Collectors.toList());
    }

    default SchoolYearResponse mapSchoolYearFromClassTerms(List<ClassesTerm> classTerms,
                                                           @Context SchoolYearMapper schoolYearMapper) {
        if (classTerms == null || classTerms.isEmpty()) return null;
        Term firstTerm = classTerms.get(0).getTerm();
        if (firstTerm == null || firstTerm.getSchoolYear() == null) return null;
        return schoolYearMapper.toSchoolYearResponse(firstTerm.getSchoolYear());
    }
}

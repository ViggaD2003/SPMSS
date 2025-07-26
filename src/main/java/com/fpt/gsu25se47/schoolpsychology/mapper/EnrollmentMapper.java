package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Enrollment;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, ClassMapper.class})
public interface EnrollmentMapper {

    @BeanMapping(builder = @Builder(disableBuilder = true))
    Enrollment toEnrollment(CreateEnrollmentRequest request, @Context Student student, @Context Classes classes);

    EnrollmentResponse toEnrollmentResponse(Enrollment enrollment);

    @AfterMapping
    default void setStudentAndClassToEnrollment(@MappingTarget Enrollment enrollment,
                                                @Context Student student,
                                                @Context Classes classes) {
        enrollment.setClasses(classes);
        enrollment.setStudent(student);
    }
}

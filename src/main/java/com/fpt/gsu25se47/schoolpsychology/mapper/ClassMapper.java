package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TeacherOfClassMapper.class})
public interface ClassMapper {

    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "students", source = "students")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    Classes mapToClass(ClassRequest request,
                       List<Student> students,
                       Teacher teacher);

    @Mapping(target = "teacher", source = "teacher")
    ClassDto mapToClassDto(Classes classes);

    @AfterMapping
    default void setStudentsToClass(@MappingTarget Classes classes,
                                    List<Student> students) {
        classes.setStudents(students);
    }
}

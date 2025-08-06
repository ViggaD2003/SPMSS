package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ActivityByCategory;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Levels;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyLevelByCategory;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DashBoardMapper {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private LevelMapper levelMapper;


    @Mappings({
            @Mapping(target = "category", source = "category.name"),
            @Mapping(target = "surveys", expression = "java(category.getSurveys().size())"),
            @Mapping(target = "programs", expression = "java(category.getSupportPrograms().size())"),
            @Mapping(target = "appointments", expression = "java(appointmentWithCategory(category))")
    })
    public abstract ActivityByCategory mapToActivityByCategory(Category category);

    @Mappings({
            @Mapping(target = "levels", expression = "java(mapToLevels(category))"),
            @Mapping(target = "category", source = "category.name")
    })
    public abstract SurveyLevelByCategory mapToSurveyLevelByCategory(Category category);

    protected List<Levels> mapToLevels(Category category) {
        return category.getLevels().stream().map(levelMapper::mapToLevelsResponse).toList();
    }

    protected int appointmentWithCategory(Category category){
       return appointmentRepository.findAllAppointmentWithCategory(category.getId()).size();
    }
}

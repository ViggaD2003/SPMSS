package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface SupportProgramMapper {

    @Mappings({
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "id", ignore = true),
    })
    @BeanMapping(builder = @Builder(disableBuilder = true))
    SupportProgram toSupportProgram(SupportProgramRequest request,
                                    Category category);

    @Mappings({
            @Mapping(target = "category", source = "supportProgram.category"),
            @Mapping(target = "sessions", expression = "java(mapSessionIds(supportProgram.getSessions()))"),
            @Mapping(target = "programRegistrations", expression = "java(mapRegistrationIds(supportProgram.getProgramRegistrations()))"),
            @Mapping(target = "programSurveys", expression = "java(mapSurveyIds(supportProgram.getProgramSurveys()))")
    })
    SupportProgramResponse toSupportProgramResponse(SupportProgram supportProgram);

    SupportProgram updateSupportProgramFromRequest(SupportProgramRequest request, @MappingTarget SupportProgram supportProgram);

    default List<Integer> mapSessionIds(List<ProgramSession> sessions) {
        return sessions == null ? new ArrayList<>() : sessions.stream().map(ProgramSession::getId).toList();
    }

    default List<Integer> mapRegistrationIds(List<ProgramParticipants> registrations) {
        return registrations == null ? new ArrayList<>() : registrations.stream().map(ProgramParticipants::getId).toList();
    }

    default List<Integer> mapSurveyIds(List<ProgramSurvey> surveys) {
        return surveys == null ? new ArrayList<>() : surveys.stream().map(ProgramSurvey::getId).toList();
    }

    @AfterMapping
    default void setSupportProgramForSessions(@MappingTarget SupportProgram supportProgram) {

        if (supportProgram.getSessions() != null) {
            supportProgram.getSessions()
                    .forEach(s -> s.setProgram(supportProgram));
        }
    }
}

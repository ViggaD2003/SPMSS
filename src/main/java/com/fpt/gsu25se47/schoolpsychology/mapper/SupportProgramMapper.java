package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SupportProgramRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SupportProgramMapper {

    @Autowired
    protected CategoryMapper categoryMapper;

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected SurveyMapper surveyMapper;

    @Autowired
    protected ParticipantMapper participantMapper;

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "hostedBy", ignore = true)
    @Mapping(target = "survey", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "programRegistrations", ignore = true)
    public abstract SupportProgram mapSupportProgram(SupportProgramRequest request);


    @Mappings({
            @Mapping(target = "category", expression = "java(mapCategory(supportProgram.getCategory()))"),
            @Mapping(target = "hostedBy", expression = "java(mapHostedBy(supportProgram.getHostedBy()))"),
            @Mapping(target = "programSurvey", expression = "java(mapSurvey(supportProgram.getSurvey()))"),
            @Mapping(target = "participants", expression = "java(supportProgram.getProgramRegistrations() == null ? 0 : supportProgram.getProgramRegistrations().size())")
    })
    public abstract SupportProgramResponse mapSupportProgramResponse(SupportProgram supportProgram);

    @Mappings({
            @Mapping(target = "category", expression = "java(mapCategory(supportProgram.getCategory()))"),
            @Mapping(target = "hostedBy", expression = "java(mapHostedBy(supportProgram.getHostedBy()))"),
            @Mapping(target = "programSurvey", expression = "java(mapSurvey(supportProgram.getSurvey()))"),
            @Mapping(target = "participants", expression = "java(supportProgram.getProgramRegistrations() == null ? null : mapToDto(supportProgram.getProgramRegistrations()))")
    })
    public abstract SupportProgramDetail mapSupportProgramDetail(SupportProgram supportProgram);


    protected CategoryResponse mapCategory(Category category) {
        if (category == null) return null;
        return categoryMapper.mapToCategorySurveyResponse(category);
    }

    protected AccountDto mapHostedBy(Account hostedBy) {
        if (hostedBy == null) return null;
        return accountMapper.toDto(hostedBy);
    }

    protected SurveyGetAllResponse mapSurvey(Survey survey) {
        if (survey == null) return null;
        return surveyMapper.mapToSurveyGetAllResponse(survey);
    }

    protected List<ProgramParticipantsResponse> mapToDto(List<ProgramParticipants> participants) {
        if (participants == null) return null;
        return participants.stream().map(participantMapper::mapToDto).toList();
    }
}

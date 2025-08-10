package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.RegisterProgramParticipantResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramStudent;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {SupportProgramMapper.class})
public interface ProgramParticipantsMapper {


    @Mapping(target = "supportProgram", source = "program")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    RegisterProgramParticipantResponse toRegisterProgramParticipantResponse(ProgramParticipants programParticipants);

}

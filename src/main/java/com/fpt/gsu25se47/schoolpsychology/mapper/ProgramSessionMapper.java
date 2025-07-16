package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramSession;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring", uses = SlotMapper.class)
public interface ProgramSessionMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "slot", source = "slot"),
            @Mapping(target = "program", source = "supportProgram"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "status", source = "request.status")
    })
    ProgramSession toProgramSession(CreateProgramSessionRequest request,
                                    Slot slot,
                                    SupportProgram supportProgram);

    @Mapping(target = "supportProgramId", source = "program.id")
    ProgramSessionResponse toProgramSessionResponse(ProgramSession programSession);
}

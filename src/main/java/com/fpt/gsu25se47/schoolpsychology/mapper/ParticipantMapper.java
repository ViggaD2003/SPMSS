package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramParticipantsResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ParticipantMapper {

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected CaseMapper caseMapper;

    @Mappings({
            @Mapping(target = "student", expression = "java(mapStudent(programParticipants.getStudent()))"),
            @Mapping(target = "cases", expression = "java(mapToCase(programParticipants.getCases()))")
    })
    public abstract ProgramParticipantsResponse mapToDto(ProgramParticipants programParticipants);


    protected AccountDto mapStudent(Account student) {
        if (student == null) return null;
        return accountMapper.toDto(student);
    }

    protected CaseGetAllResponse mapToCase(Cases cases) {
        if(cases == null) return null;
        return caseMapper.mapToCaseGetAllResponse(cases, null);
    }
}

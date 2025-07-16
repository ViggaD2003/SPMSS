package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ProgramRegistrationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramRegistrationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramRegistration;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { AccountMapper.class })
public interface ProgramRegistrationMapper {

    @Mapping(target = "registeredAt", expression = "java(LocalDate.now())")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    ProgramRegistration toProgramRegistration(ProgramRegistrationRequest request,
                                              @Context SupportProgram program,
                                              @Context Account account);

    @Mapping(target = "supportProgramId", source = "program.id")
    @Mapping(target = "supportProgramName", source = "program.name")
    @Mapping(target = "programStartDate", source = "program.startDate")
    @Mapping(target = "programEndDate", source = "program.endDate")
    @Mapping(target = "location", source = "program.location")
    @Mapping(target = "isOnline", source = "program.isOnline")
    @Mapping(target = "account", source = "account")
    ProgramRegistrationResponse toProgramRegistrationResponse(ProgramRegistration programRegistration);

    @AfterMapping
    default void setAccountAndProgramToProgramRegistration(@MappingTarget ProgramRegistration registration,
                                                 @Context SupportProgram program,
                                                 @Context Account account) {

        registration.setProgram(program);
        registration.setAccount(account);
    }

    ProgramRegistration updateProgramRegistrationFromRequest(ProgramRegistrationRequest request,
                                                             @MappingTarget ProgramRegistration programRegistration);
}

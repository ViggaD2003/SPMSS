package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ProgramSessionMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "slot", source = "slot"),
            @Mapping(target = "program", source = "supportProgram"),
            @Mapping(target = "hostBy", source = "hostBy"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "status", source = "request.status")
    })
    ProgramSession toProgramSession(CreateProgramSessionRequest request,
                                    Slot slot,
                                    SupportProgram supportProgram,
                                    Account hostBy);

    @Mapping(target = "supportProgramId", source = "program.id")
    @Mapping(target = "hostBy", expression = "java(setAccountDto(programSession.getHostBy()))")
    ProgramSessionResponse toProgramSessionResponse(ProgramSession programSession,
                                                    @Context Account account);

    default AccountDto setAccountDto(Account account) {

        if (account != null) {
            switch (account.getRole()) {
                case TEACHER -> {
                    return getTeacherDto(account);
                }
                case COUNSELOR -> {
                    return getCounselorDto(account);
                }
            }
        }
        return null;
    }

    private static CounselorDto getCounselorDto(Account account) {
        Counselor counselor = account.getCounselor();
        CounselorDto counselorDto = new CounselorDto();
        counselorDto.setCounselorCode(counselor.getCounselorCode());
        counselorDto.setLinkMeet(counselor.getLinkMeet());
        counselorDto.setGender(account.getGender());
        counselorDto.setEmail(account.getEmail());
        counselorDto.setFullName(account.getFullName());
        counselorDto.setPhoneNumber(account.getPhoneNumber());
        return counselorDto;
    }

    private static TeacherDto getTeacherDto(Account account) {
        Teacher teacher = account.getTeacher();
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setGender(account.getGender());
        teacherDto.setEmail(account.getEmail());
        teacherDto.setFullName(account.getFullName());
        teacherDto.setPhoneNumber(account.getPhoneNumber());
        teacherDto.setTeacherCode(teacher.getTeacherCode());
        teacherDto.setLinkMeet(teacher.getLinkMeet());
        return teacherDto;
    }
}

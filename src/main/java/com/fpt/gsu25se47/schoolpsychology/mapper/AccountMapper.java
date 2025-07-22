package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Autowired
    private StudentMapper studentMapper;

    public AccountDto toDto(Account account) {
        if (account != null) {
            return switch (account.getRole()) {
                case STUDENT -> studentMapper.mapStudentWithoutEvaluations(account.getStudent());
                case TEACHER -> toTeacherDto(account);
                case COUNSELOR -> toCounselorDto(account);
                default -> null;
            };
        }
        return null;
    }

    private TeacherDto toTeacherDto(Account account) {
        Teacher teacher = account.getTeacher();
        if (teacher == null) return null;

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setGender(account.getGender());
        teacherDto.setEmail(account.getEmail());
        teacherDto.setFullName(account.getFullName());
        teacherDto.setPhoneNumber(account.getPhoneNumber());
        teacherDto.setTeacherCode(teacher.getTeacherCode());
        teacherDto.setLinkMeet(teacher.getLinkMeet());
        return teacherDto;
    }

    public CounselorDto toCounselorDto(Account account) {
        Counselor counselor = account.getCounselor();
        if (counselor == null) return null;

        CounselorDto counselorDto = new CounselorDto();
        counselorDto.setId(counselor.getAccount().getId());
        counselorDto.setCounselorCode(counselor.getCounselorCode());
        counselorDto.setLinkMeet(counselor.getLinkMeet());
        counselorDto.setGender(account.getGender());
        counselorDto.setEmail(account.getEmail());
        counselorDto.setFullName(account.getFullName());
        counselorDto.setPhoneNumber(account.getPhoneNumber());
        return counselorDto;
    }
}


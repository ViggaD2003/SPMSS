package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CounselorMapper counselorMapper;

    public AccountDto toDto(Account account) {
        if (account != null) {
            return switch (account.getRole()) {
                case STUDENT -> studentMapper.mapStudentDto(account.getStudent());
                case TEACHER -> teacherMapper.toTeacherOfClassDto(account.getTeacher());
                case COUNSELOR -> counselorMapper.toCounselorDto(account.getCounselor());
                default -> null;
            };
        }
        return null;
    }

}
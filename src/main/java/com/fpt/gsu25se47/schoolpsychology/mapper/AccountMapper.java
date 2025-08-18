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

    @Autowired
    private ParentMapper parentMapper;

    public AccountDto toDto(Account account) {
        if (account != null) {
            return switch (account.getRole()) {
                case STUDENT -> studentMapper.mapStudentDtoWithClass(account.getStudent());
                case TEACHER -> teacherMapper.toTeacherDto(account.getTeacher());
                case COUNSELOR -> counselorMapper.mapToCounselorDto(account.getCounselor());
                case PARENTS -> parentMapper.toParentWithRelationshipDto(account.getGuardian());
                default -> null;
            };
        }
        return null;
    }

}
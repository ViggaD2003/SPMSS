package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CounselorMapper counselorMapper;
    @Autowired
    private ParentMapper parentMapper;

    public AccountDto toDto(Account account) {
        if (account != null) {
            return switch (account.getRole()) {
                case STUDENT -> {
                    Classes activeClass = classRepository.findActiveClassByStudentId(account.getId());
                    yield studentMapper.mapStudentDtoWithClass(account.getStudent(), activeClass, classMapper);
                }
                case TEACHER -> teacherMapper.toTeacherOfClassDto(account.getTeacher());
                case COUNSELOR -> counselorMapper.toCounselorDto(account.getCounselor());
                case PARENTS -> parentMapper.toParentWithRelationshipDto(account.getGuardian());
                default -> null;
            };
        }
        return null;
    }

}
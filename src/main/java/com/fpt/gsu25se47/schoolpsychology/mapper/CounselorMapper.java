package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CounselorMapper {

    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "id", source = "account.id")
    CounselorDto toCounselorDto(Counselor counselor);
}

package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentBaseResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Guardian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParentMapperInf {

    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    ParentBaseResponse toParentBaseResponse(Guardian guardian);
}

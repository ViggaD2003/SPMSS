package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CounselorMapper {

    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "counselorCode", source = "counselorCode")
    @Mapping(target = "linkMeet", source = "linkMeet")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    CounselorDto mapToCounselorDto(Counselor counselor);
}

package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CounselorMapper {

    @Named("toCounselorDto")
    @Mapping(target = "roleName", source = "account.role")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "hasAvailable", ignore = true)
    CounselorDto toCounselorDto(Counselor counselor);

    default CounselorDto mapToCounselorDto(Counselor counselor){
        CounselorDto counselorDto = toCounselorDto(counselor);

        Set<Integer> setCategory = new HashSet<>();

        List<Cases> activeCase = counselor.getAccount().getCounselorCases()
                .stream()
                .filter(t -> {
                    if (t.getStatus() != Status.CLOSED) {
                        setCategory.add(t.getInitialLevel().getCategory().getId());
                        return true;
                    } else
                        return false;
                }).toList();

        counselorDto.setCategories(setCategory);
        counselorDto.setHasAvailable(!activeCase.isEmpty());

        return counselorDto;
    }
}

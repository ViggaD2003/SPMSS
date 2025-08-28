package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class CounselorMapper {

    @Autowired
    private CaseRepository caseRepository;

    @Named("toCounselorDto")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "roleName", source = "account.role")
    @Mapping(target = "gender", source = "account.gender")
    @Mapping(target = "dob", source = "account.dob")
    @Mapping(target = "phoneNumber", source = "account.phoneNumber")
    @Mapping(target = "fullName", source = "account.fullName")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "hasAvailable", ignore = true)
    public abstract CounselorDto toCounselorDto(Counselor counselor);

    public CounselorDto mapToCounselorDto(Counselor counselor) {
        CounselorDto counselorDto = toCounselorDto(counselor);

        Set<Integer> setCategory = new HashSet<>();

//        List<Cases> activeCase = counselor.getAccount().getCounselorCases()
//                .stream()
//                .filter(t -> {
//                    if (t.getStatus() != Status.CLOSED) {
//                        setCategory.add(t.getInitialLevel().getCategory().getId());
//                        return true;
//                    }
//                    return false;
//                }).toList();

        List<Cases> activeCases = caseRepository.findAllActiveCases(counselor.getId());

        activeCases.forEach(activeCase -> {
            setCategory.add(activeCase.getInitialLevel().getCategory().getId());
        });

        counselorDto.setCategories(setCategory);
        counselorDto.setHasAvailable(!activeCases.isEmpty());

        return counselorDto;
    }
}

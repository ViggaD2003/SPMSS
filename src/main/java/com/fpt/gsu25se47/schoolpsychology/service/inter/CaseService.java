package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;

import java.util.Optional;

public interface CaseService {

    Optional<?> createCase(AddNewCaseDto dto);

}

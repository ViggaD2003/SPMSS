package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubTypeRequest;
import java.util.Optional;

public interface SubTypeService {

    Optional<?> getAllSubTypes();

    Optional<?> addNewSubType(SubTypeRequest subTypeRequest);
}

package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateTermRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Term.TermResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermMapper {

    TermResponse toTermResponse(Term term);

    @Mapping(target = "schoolYear", ignore = true)
    @Mapping(target = "id", ignore = true)
    Term toTerm(CreateTermRequest request);
}

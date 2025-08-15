package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Term.TermResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Term;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermMapper {

    TermResponse toTermResponse(Term term);
}

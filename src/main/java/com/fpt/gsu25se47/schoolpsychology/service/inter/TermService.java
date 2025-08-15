package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.model.Term;

import java.util.List;

public interface TermService {

    List<Term> getTermsByYearId(Integer yearId);
}

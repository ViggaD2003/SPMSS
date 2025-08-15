package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.model.Term;
import com.fpt.gsu25se47.schoolpsychology.repository.TermRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermServiceImpl implements TermService {

    private final TermRepository termRepository;

    @Override
    public List<Term> getTermsByYearId(Integer yearId) {

        return termRepository.findAllByYearId(yearId);
    }
}

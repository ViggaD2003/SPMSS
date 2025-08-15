package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SchoolYearMapper;
import com.fpt.gsu25se47.schoolpsychology.repository.SchoolYearRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolYearServiceImpl implements SchoolYearService {

    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearMapper schoolYearMapper;

    @Override
    public List<SchoolYearResponse> getSchoolYears() {

        return schoolYearRepository.findAll()
                .stream()
                .map(schoolYearMapper::toSchoolYearResponse)
                .toList();
    }
}

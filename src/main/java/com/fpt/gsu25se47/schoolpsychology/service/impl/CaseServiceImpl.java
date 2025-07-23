package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    private final AccountRepository accountRepository;

    @Override
    public Optional<?> createCase(AddNewCaseDto dto) {
        Account student = accountRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if(student.getRole().name() != "STUDENT"){
            throw new IllegalArgumentException("Account is not STUDENT");
        }

        Account createBy = accountRepository.findById(dto.getCreateBy())
                .orElseThrow(() -> new IllegalArgumentException("Create By not found"));



        Cases cases = Cases.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(Status.NEW)
                .progressTrend(dto.getProgressTrend())
                .build();
    }
}

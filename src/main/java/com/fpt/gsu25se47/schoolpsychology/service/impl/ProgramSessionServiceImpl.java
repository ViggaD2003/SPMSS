package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramSessionMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProgramSessionServiceImpl implements ProgramSessionService {

    private final ProgramSessionRepository programSessionRepository;
    private final SlotRepository slotRepository;
    private final SupportProgramRepository supportProgramRepository;
    private final AccountRepository accountRepository;
    private final CounselorRepository counselorRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramSessionMapper programSessionMapper;

    @Override
    @Transactional
    public ProgramSessionResponse createProgramSession(CreateProgramSessionRequest request) {

        SupportProgram supportProgram = supportProgramRepository.findById(request.getSupportProgramId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Support program not found with ID: " + request.getSupportProgramId()));

        Slot slot = slotRepository.findById(request.getSlotId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found with ID: " + request.getSupportProgramId()));

        Account account = accountRepository.findById(request.getHostById()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with ID: " + request.getHostById()));

        Teacher teacher = null;
        Counselor counselor = null;

        switch (account.getRole()) {
            case COUNSELOR ->
                    counselor = counselorRepository.findById(account.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Counselor not found with ID: " + request.getHostById()));
            case TEACHER ->
                    teacher = teacherRepository.findById(account.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found with ID: " + request.getHostById()));
            default -> throw new IllegalStateException("Unexpected value: " + account.getRole());
        }
        ;

        ProgramSession programSession = programSessionMapper.toProgramSession(request, slot, supportProgram, account);

        return programSessionMapper.toProgramSessionResponse(
                programSessionRepository.save(programSession),
                teacher,
                counselor);
    }
}

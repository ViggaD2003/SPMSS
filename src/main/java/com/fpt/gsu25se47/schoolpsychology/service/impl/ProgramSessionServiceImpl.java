package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramSessionMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramSession;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

        ProgramSession programSession = programSessionMapper.toProgramSession(request, slot, supportProgram, account);

        return programSessionMapper.toProgramSessionResponse(
                programSessionRepository.save(programSession), account);
    }

    @Override
    public List<ProgramSessionResponse> getAllProgramSessionsBySupportProgramId(Integer supportProgramId) {

        List<ProgramSession> programSessions = programSessionRepository.findAllByProgramId(supportProgramId);

        return programSessions.stream()
                .map(t -> programSessionMapper.toProgramSessionResponse(t,
                        t.getHostBy()))
                .toList();
    }

    @Override
    public List<ProgramSessionResponse> getAllProgramSessions() {

        return programSessionRepository.findAll()
                .stream()
                .map(t -> programSessionMapper.toProgramSessionResponse(t, t.getHostBy()))
                .toList();
    }

    @Override
    public ProgramSessionResponse getProgramSessionById(Integer id) {

        ProgramSession programSession = programSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program session not found with ID: " + id
                ));

        return programSessionMapper.toProgramSessionResponse(programSession, programSession.getHostBy());
    }

    @Override
    public void deleteProgramSession(Integer id) {

        ProgramSession programSession = programSessionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Program session not found with ID: " + id));

        programSessionRepository.delete(programSession);
    }
}

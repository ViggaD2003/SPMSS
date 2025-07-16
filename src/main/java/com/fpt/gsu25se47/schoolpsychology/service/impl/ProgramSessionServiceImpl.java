package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateProgramSessionRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramSessionResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramSessionMapper;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramSession;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramSessionService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import com.fpt.gsu25se47.schoolpsychology.validations.SlotValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramSessionServiceImpl implements ProgramSessionService {

    private final SlotService slotService;
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

        SlotValidator.validateSlotWithinSession(request.getCreateSlotRequest().getStartDateTime(),
                request.getCreateSlotRequest().getEndDateTime(),
                request.getDate());

        Slot slot = slotService.createSlot(request.getCreateSlotRequest());

        ProgramSession programSession = programSessionMapper.toProgramSession(request, slot, supportProgram);

        return programSessionMapper.toProgramSessionResponse(
                programSessionRepository.save(programSession));
    }

    @Override
    public List<ProgramSessionResponse> getAllProgramSessionsBySupportProgramId(Integer supportProgramId) {

        List<ProgramSession> programSessions = programSessionRepository.findAllByProgramId(supportProgramId);

        return programSessions.stream()
                .map(programSessionMapper::toProgramSessionResponse)
                .toList();
    }

    @Override
    public List<ProgramSessionResponse> getAllProgramSessions() {

        return programSessionRepository.findAll()
                .stream()
                .map(programSessionMapper::toProgramSessionResponse)
                .toList();
    }

    @Override
    public ProgramSessionResponse getProgramSessionById(Integer id) {

        ProgramSession programSession = programSessionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program session not found with ID: " + id
                ));

        return programSessionMapper.toProgramSessionResponse(programSession);
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

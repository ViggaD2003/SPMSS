package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ProgramRegistrationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ProgramRegistrationResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ProgramRegistrationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.ProgramRegistration;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ProgramRegistrationRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ProgramRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramRegistrationServiceImpl implements ProgramRegistrationService {

    private final ProgramRegistrationRepository programRegistrationRepository;
    private final SupportProgramRepository supportProgramRepository;
    private final AccountRepository accountRepository;
    private final ProgramRegistrationMapper mapper;

    @Override
    @Transactional
    public ProgramRegistrationResponse createProgramRegistration(ProgramRegistrationRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Account not found"));

        SupportProgram supportProgram = supportProgramRepository.findById(request.getSupportProgramId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Support program not found with ID: " + request.getSupportProgramId()));

        int currentRegistrations = programRegistrationRepository.countByProgramId(supportProgram.getId());

        if (supportProgram.getMaxParticipants() != null && currentRegistrations >= supportProgram.getMaxParticipants()) {
            throw new IllegalStateException("This program has reached the maximum number of participants.");
        }

        boolean alreadyRegistered = programRegistrationRepository.existsByProgramIdAndAccountId(supportProgram.getId(), account.getId());

        if (alreadyRegistered) {
            throw new IllegalStateException("This account is already registered for the program.");
        }

        ProgramRegistration programRegistration = mapper.toProgramRegistration(request, supportProgram,
                account);

        return mapper.toProgramRegistrationResponse(
                programRegistrationRepository.save(programRegistration)
        );
    }

    @Override
    public List<ProgramRegistrationResponse> getAllProgramRegistrationsByProgramId(Integer supportProgramId) {

        List<ProgramRegistration> registrations = programRegistrationRepository.findAllByProgramId(supportProgramId);

        return registrations.stream()
                .map(mapper::toProgramRegistrationResponse)
                .toList();
    }

    @Override
    public List<ProgramRegistrationResponse> getAllProgramRegistrationsByAccountId(Integer accountId) {

        List<ProgramRegistration> programRegistrations = programRegistrationRepository.findAllByAccountId(accountId);

        return programRegistrations.stream()
                .map(mapper::toProgramRegistrationResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProgramRegistrationResponse updateProgramRegistration(Integer programRegistrationId, ProgramRegistrationRequest request) {

        ProgramRegistration programRegistration = programRegistrationRepository
                .findById(programRegistrationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program registration not found with ID: " + programRegistrationId
                ));


        ProgramRegistration programRegistrationUpdated = mapper.updateProgramRegistrationFromRequest(request,
                programRegistration);

        return mapper.toProgramRegistrationResponse(programRegistrationUpdated);
    }

    @Override
    public ProgramRegistrationResponse getById(Integer programRegistrationId) {

        ProgramRegistration programRegistration = programRegistrationRepository
                .findById(programRegistrationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program registration not found with ID: " + programRegistrationId
                ));

        return mapper.toProgramRegistrationResponse(programRegistration);
    }


}

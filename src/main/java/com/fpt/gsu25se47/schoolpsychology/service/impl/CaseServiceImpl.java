package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewCaseDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.CaseMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.SurveyCaseLink;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.CaseService;
import com.fpt.gsu25se47.schoolpsychology.utils.CurrentAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaseServiceImpl implements CaseService {
    private final CaseRepository caseRepository;

    private final AccountRepository accountRepository;

    private final LevelRepository levelRepository;

    private final SurveyCaseLinkRepository surveyCaseLinkRepository;

    private final SurveyRepository surveyRepository;

    private final CaseMapper caseMapper;

    @Override
    public Optional<?> createCase(AddNewCaseDto dto) {
        Account student = accountRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if(student.getRole().name() != "STUDENT"){
            throw new IllegalArgumentException("Account is not STUDENT");
        }

        if(!caseRepository.isStudentFreeFromOpenCases(dto.getStudentId())){
            throw new IllegalArgumentException("Student is not available to open cases");
        }

        Account createBy = accountRepository.findById(dto.getCreateBy())
                .orElseThrow(() -> new IllegalArgumentException("Create By not found"));
        Cases cases = Cases.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(Status.NEW)
                .progressTrend(dto.getProgressTrend())
                .createBy(createBy)
                .student(student)
                .initialLevel(levelRepository.findById(dto.getInitialLevelId()).orElseThrow(() -> new IllegalArgumentException("Initial Level not found")))
                .currentLevel(levelRepository.findById(dto.getCurrentLevelId()).orElseThrow(() -> new IllegalArgumentException("Current Level not found")))
                .progressTrend(dto.getProgressTrend())
                .build();

        caseRepository.save(cases);
        return Optional.of("Case created !");
    }

    @Override
    public Optional<?> assignCounselor(Integer counselorId, Integer caseId) {
       Account account = accountRepository.findById(counselorId)
               .orElseThrow(() -> new IllegalArgumentException("Account not found"));

       Cases cases = caseRepository.findById(caseId)
               .orElseThrow(() -> new IllegalArgumentException("Cases is not found"));

       cases.setCounselor(account);
       cases.setStatus(Status.IN_PROGRESS);

       caseRepository.save(cases);

       return Optional.of("Case assigned successfully !");
    }

    @Override
    public Optional<?> getAllCases(List<String> statusCase, Integer categoryId) {
        UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Account account = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        List<Cases> filteredCases = caseRepository.findAllCasesByRoleAndAccountWithStatusSorted(account.getRole().name(), account.getId(), statusCase, statusCase.size(), categoryId);

        return Optional.of(
                filteredCases.stream()
                        .map(caseMapper::mapToCaseGetAllResponse)
                        .toList()
        );
    }

    @Override
    public Optional<?> getAllCaseByCategory(Integer categoryId) {
        List<Cases> cases = caseRepository.findAllByCategoryId(categoryId);

        List<CaseGetAllResponse> casesResponse = cases.stream()
                .map(caseMapper::mapToCaseGetAllResponse)
                .toList();
        return Optional.of(casesResponse);
    }

    @Override
    public Optional<?> getDetailById(Integer caseId) {
        return Optional.empty();
    }

    @Override
    public Optional<?> addSurveyCaseLink(List<Integer> caseIds, Integer surveyId) {
        try{
            UserDetails userDetails = CurrentAccountUtils.getCurrentUser();
            if (userDetails == null) {
                throw new BadRequestException("Unauthorized");
            }

            Account assignedBy = accountRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new BadRequestException("Unauthorized"));

            List<Cases> cases = caseRepository.findAll();
            List<Cases> filteredCases = cases.stream()
                    .filter(item -> caseIds.contains(item.getId()))
                    .toList();
            Survey survey = surveyRepository.findById(surveyId)
                    .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

            filteredCases.forEach(item -> {
                SurveyCaseLink surveyCaseLink = SurveyCaseLink.builder()
                        .cases(item)
                        .survey(survey)
                        .assignedBy(assignedBy)
                        .isActive(true)
                        .build();
                surveyCaseLinkRepository.save(surveyCaseLink);
            });

            return Optional.of("Added survey case link successfully !");
        } catch (Exception e){
            log.error("Failed to create survey case link: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create survey case link");
        }
    }

    @Override
    public Optional<?> removeSurveyCaseLink(List<Integer> caseIds) {
        List<SurveyCaseLink> surveyCaseLinks = surveyCaseLinkRepository.findAll();
        List<SurveyCaseLink> filteredSurveyCaseLink = surveyCaseLinks.stream()
                .filter(item -> caseIds.contains(item.getCases().getId()))
                .toList();

        filteredSurveyCaseLink.forEach(item -> {
            item.setIsActive(false);
            item.setRemoveAt(LocalDate.now());
            surveyCaseLinkRepository.save(item);
        });

        return Optional.of("Removed survey case link successfully !");

    }




}

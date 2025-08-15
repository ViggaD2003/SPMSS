package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSchoolYearRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateTermRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SchoolYearMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.TermMapper;
import com.fpt.gsu25se47.schoolpsychology.model.SchoolYear;
import com.fpt.gsu25se47.schoolpsychology.model.Term;
import com.fpt.gsu25se47.schoolpsychology.repository.SchoolYearRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TermRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SchoolYearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolYearServiceImpl implements SchoolYearService {

    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearMapper schoolYearMapper;

    private final TermRepository termRepository;
    private final TermMapper termMapper;

    @Override
    public List<SchoolYearResponse> getSchoolYears() {

        return schoolYearRepository.findAll()
                .stream()
                .map(schoolYearMapper::toSchoolYearResponse)
                .toList();
    }

    @Override
    public SchoolYearResponse createSchoolYear(CreateSchoolYearRequest request) {

        log.info("Creating school year: {}", request.getName());

        // Validation
        validateSchoolYearRequest(request);

        // Create and save school year
        SchoolYear schoolYear = schoolYearMapper.toSchoolYear(request);
        SchoolYear savedSchoolYear = schoolYearRepository.save(schoolYear);

        // Create and save terms
        if (request.getTermRequests() != null && !request.getTermRequests().isEmpty()) {
            List<Term> terms = createTerms(request.getTermRequests(), savedSchoolYear);
            savedSchoolYear.setTerms(terms);
        }

        log.info("Successfully created school year with ID: {}", savedSchoolYear.getId());
        return schoolYearMapper.toSchoolYearResponse(savedSchoolYear);
    }

    private void validateSchoolYearRequest(CreateSchoolYearRequest request) {
        // Basic validation
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (StringUtils.isBlank(request.getName())) {
            throw new IllegalArgumentException("School year name is required");
        }

        // Check for duplicate name
        if (schoolYearRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("School year with name '" + request.getName() + "' already exists");
        }

        // Check for overlapping school years
        List<SchoolYear> overlapping = schoolYearRepository.findOverlappingSchoolYears(
                request.getStartDate(), request.getEndDate());
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("School year dates overlap with existing school year(s)");
        }

        // Validate terms
        if (request.getTermRequests() != null) {
            validateTermRequests(request.getTermRequests(), request.getStartDate(), request.getEndDate());
        }
    }

    private void validateTermRequests(List<CreateTermRequest> termRequests,
                                      LocalDate schoolYearStart, LocalDate schoolYearEnd) {
        if (termRequests.isEmpty()) {
            return;
        }

        // Check for duplicate term numbers
        Set<Integer> termNumbers = new HashSet<>();
        for (CreateTermRequest termRequest : termRequests) {
            if (termRequest.getTermNumber() == null) {
                throw new IllegalArgumentException("Term number is required");
            }

            if (!termNumbers.add(termRequest.getTermNumber())) {
                throw new IllegalArgumentException("Duplicate term number: " + termRequest.getTermNumber());
            }

            // Validate term dates
            if (termRequest.getStartDate() == null || termRequest.getEndDate() == null) {
                throw new IllegalArgumentException("Term start and end dates are required");
            }

            if (termRequest.getStartDate().isAfter(termRequest.getEndDate())) {
                throw new IllegalArgumentException("Term start date must be before end date for term " + termRequest.getTermNumber());
            }

            // Check if term dates are within school year dates
            if (termRequest.getStartDate().isBefore(schoolYearStart) ||
                    termRequest.getEndDate().isAfter(schoolYearEnd)) {
                throw new IllegalArgumentException("Term " + termRequest.getTermNumber() +
                        " dates must be within school year dates");
            }
        }

        // Check for overlapping terms
        termRequests.sort(Comparator.comparing(CreateTermRequest::getStartDate));
        for (int i = 0; i < termRequests.size() - 1; i++) {
            CreateTermRequest current = termRequests.get(i);
            CreateTermRequest next = termRequests.get(i + 1);

            if (current.getEndDate().isAfter(next.getStartDate()) ||
                    current.getEndDate().equals(next.getStartDate())) {
                throw new IllegalArgumentException("Terms " + current.getTermNumber() +
                        " and " + next.getTermNumber() + " have overlapping dates");
            }
        }
    }

    private List<Term> createTerms(List<CreateTermRequest> termRequests, SchoolYear schoolYear) {
        List<Term> terms = new ArrayList<>();

        for (CreateTermRequest termRequest : termRequests) {
            Term term = termMapper.toTerm(termRequest);
            term.setSchoolYear(schoolYear);
            terms.add(term);
        }

        return termRepository.saveAll(terms);
    }
}

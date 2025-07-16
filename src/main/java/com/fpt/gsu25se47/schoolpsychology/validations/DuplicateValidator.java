package com.fpt.gsu25se47.schoolpsychology.validations;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ReportCategoryRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DuplicateValidator {

    public void validateAnswerIds(List<SubmitAnswerRecordRequest> submitAnswerRecordRequests) {
        Set<Integer> seenAnswerIds = new HashSet<>();

        for (SubmitAnswerRecordRequest submit : submitAnswerRecordRequests) {
            if (!seenAnswerIds.add(submit.getAnswerId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate answerId found: " + submit.getAnswerId()
                );
            }
        }
    }

    public void validateCategoryIds(List<ReportCategoryRequest> reportCategoryRequests) {
        Set<Integer> seenCategoryIds = new HashSet<>();

        for (ReportCategoryRequest request : reportCategoryRequests) {
            if (!seenCategoryIds.add(request.getCategoryId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate categoryId found: " + request.getCategoryId()
                );
            }
        }
    }

    public void validateStudentCodes(List<String> studentCodes) {
        Set<String> seenStudentCodes = new HashSet<>();

        for (String sd : studentCodes) {
            if (!seenStudentCodes.add(sd)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate studentCodes found: " + sd
                );
            }
        }
    }
}

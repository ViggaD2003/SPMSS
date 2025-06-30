package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fpt.gsu25se47.schoolpsychology.dto.request.SubmitAnswerRecordRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AnswerRecordUtil {

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
}

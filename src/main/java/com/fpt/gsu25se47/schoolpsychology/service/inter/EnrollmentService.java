package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {

    List<EnrollmentResponse> createBulkEnrollment(CreateEnrollmentRequest request);
}

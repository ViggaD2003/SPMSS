package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.AppointmentRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentRole;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RecordStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.DuplicateValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AppointmentRecordServiceImpl implements AppointmentRecordService {

    private final AppointmentRecordRepository appointmentRecordRepository;
    private final AppointmentRecordMapper appointmentRecordMapper;
    private final DuplicateValidationUtils duplicateValidationUtils;
    private final MentalEvaluationServiceImpl mentalEvaluationService;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public AppointmentRecordResponse createAppointmentRecord(CreateAppointmentRecordRequest request) {

        AppointmentRecord appointmentRecord = appointmentRecordMapper.toAppointmentRecord(request);

        AppointmentRecord savedAppointmentRecord = appointmentRecordRepository.save(appointmentRecord);

        if (request.getReportCategoryRequests() != null) {

            duplicateValidationUtils.validateCategoryIds(request.getReportCategoryRequests());

            int evaluationRecordId = appointmentRecord.getId();
            LocalDate createdDate = appointmentRecord.getCreatedDate().toLocalDate();
            EvaluationType evaluationType = EvaluationType.APPOINTMENT;
            int studentId = appointmentRecord.getAppointment().getBookedFor().getId();

            request.getReportCategoryRequests()
                    .forEach((t) -> {
                        CreateMentalEvaluationRequest mentalEvaluationRequest = CreateMentalEvaluationRequest.builder()
                                .evaluationRecordId(evaluationRecordId)
                                .date(createdDate)
                                .evaluationType(evaluationType)
                                .totalScore(t.getScore())
                                .studentId(studentId)
                                .categoryId(t.getCategoryId())
                                .build();

                        mentalEvaluationService.createMentalEvaluation(mentalEvaluationRequest);
                    });
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment not found with ID: " + request.getAppointmentId()));

        if (request.getStatus() != RecordStatus.CANCELED) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentRepository.save(appointment);
        }

        return appointmentRecordMapper.toAppointmentRecordResponse(savedAppointmentRecord);
    }

    @Override
    public AppointmentRecordResponse getAppointmentRecordById(int appointmentRecordId) {

        return appointmentRecordMapper.toAppointmentRecordResponse(
                appointmentRecordRepository.findById(appointmentRecordId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Appointment record not found with ID: " + appointmentRecordId)
                        )
        );
    }

    @Override
    public Page<AppointmentRecordResponse> getAllAppointmentRecords(Pageable pageable) {

        Page<AppointmentRecord> appointmentRecords = appointmentRecordRepository.findAll(pageable);

        return appointmentRecords.map(appointmentRecordMapper::toAppointmentRecordResponse);
    }

    @Override
    public Page<AppointmentRecordResponse> getAppointmentRecordsByField(AppointmentRole field, int accountId, Pageable pageable) {
        Page<AppointmentRecord> records;

        switch (field) {
            case BOOKED_FOR -> records = appointmentRecordRepository.findAllByBookedFor(accountId, pageable);
            case BOOKED_BY -> records = appointmentRecordRepository.findAllByBookedBy(accountId, pageable);
//            case HOSTED_BY -> records = appointmentRecordRepository.findAllByHostBy(accountId);
            default -> throw new IllegalArgumentException("Invalid field name: " + field);
        }

        return records.map(appointmentRecordMapper::toAppointmentRecordResponse);
    }
}

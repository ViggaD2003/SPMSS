package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.AppointmentRecordMapper;
import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentRole;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRecordRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentRecordService;
import com.fpt.gsu25se47.schoolpsychology.utils.AnswerRecordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentRecordServiceImpl implements AppointmentRecordService {

    private final AppointmentRecordRepository appointmentRecordRepository;
    private final AppointmentRecordMapper appointmentRecordMapper;
    private final AnswerRecordUtil answerRecordUtil;

    @Override
    public AppointmentRecordResponse createAppointmentRecord(CreateAppointmentRecordRequest request) {

        answerRecordUtil.validateAnswerIds(request.getAnswerRecordRequests());

        AppointmentRecord appointmentRecord = appointmentRecordMapper.toAppointmentRecord(request);
        appointmentRecord.getAnswerRecords().forEach(ar -> ar.setAppointmentRecord(appointmentRecord));

        AppointmentRecord savedAppointmentRecord = appointmentRecordRepository.save(appointmentRecord);

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

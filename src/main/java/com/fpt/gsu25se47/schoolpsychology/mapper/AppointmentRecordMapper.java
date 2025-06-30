package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAnswerRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.AnswerRecord;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentRecordMapper {

    private final AppointmentRepository appointmentRepository;
    private final AnswerRecordMapper answerRecordMapper;

    public AppointmentRecord toAppointmentRecord(CreateAppointmentRecordRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment not found with ID: " + request.getAppointmentId()));

        List<AnswerRecord> answerRecords = request.getAnswerRecordRequests()
                .stream()
                .map(t -> {
                    var createAnswerRecordRequest = CreateAnswerRecordRequest.builder()
                            .submitAnswerRecordRequests(t)
                            .build();
                    return answerRecordMapper.mapToAnswerRecord(createAnswerRecordRequest);
                })
                .toList();

        return AppointmentRecord.builder()
                .appointment(appointment)
                .answerRecords(answerRecords)
                .status(request.getStatus())
                .noteSuggest(request.getNoteSuggest())
                .noteSummary(request.getNoteSummary())
                .sessionFlow(request.getSessionFlow())
                .studentCoopLevel(request.getStudentCoopLevel())
                .totalScore(request.getTotalScore())
                .build();
    }

    public AppointmentRecordResponse toAppointmentRecordResponse(AppointmentRecord appointmentRecord) {

        return AppointmentRecordResponse.builder()
                .id(appointmentRecord.getId())
                .appointmentId(appointmentRecord.getAppointment().getId())
                .status(appointmentRecord.getStatus().name())
                .totalScore(appointmentRecord.getTotalScore())
                .studentCoopLevel(appointmentRecord.getStudentCoopLevel().name())
                .sessionFlow(appointmentRecord.getSessionFlow().name())
                .noteSummary(appointmentRecord.getNoteSummary())
                .answerRecords(appointmentRecord.getAnswerRecords()
                        .stream()
                        .map(answerRecordMapper::mapToAnswerRecordResponse)
                        .toList())
                .build();
    }

}

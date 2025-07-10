package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRecordRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentRecordResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AppointmentRecordMapper {

    private final AppointmentRepository appointmentRepository;
    private final AnswerRecordMapper answerRecordMapper;
    private final AppointmentMapper appointmentMapper;

    public AppointmentRecord toAppointmentRecord(CreateAppointmentRecordRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment not found with ID: " + request.getAppointmentId()));

        //        List<AnswerRecord> answerRecords = request.getAnswerRecordRequests()
//                .stream()
//                .map(t -> {
//                    var createAnswerRecordRequest = CreateAnswerRecordRequest.builder()
//                            .submitAnswerRecordRequests(t)
//                            .build();
//                    return answerRecordMapper.mapToAnswerRecord(createAnswerRecordRequest);
//                })
//                .toList();

        return AppointmentRecord.builder()
                .appointment(appointment)
//                .answerRecords(answerRecords)
                .status(request.getStatus())
                .noteSuggest(request.getNoteSuggest())
                .noteSummary(request.getNoteSummary())
                .sessionFlow(request.getSessionFlow())
                .studentCoopLevel(request.getStudentCoopLevel())
//                .sessionFlow(Optional.ofNullable(request.getSessionFlow()).orElse(SessionFlow.UNKNOWN))
//                .studentCoopLevel(Optional.ofNullable(request.getStudentCoopLevel()).orElse(StudentCoopLevel.UNKNOWN))
                .totalScore(request.getTotalScore())
                .reason(request.getReason())
                .build();
    }

    public AppointmentRecordResponse toAppointmentRecordResponse(AppointmentRecord appointmentRecord) {

        return AppointmentRecordResponse.builder()
                .id(appointmentRecord.getId())
                .appointment(appointmentMapper.mapToResponse(appointmentRecord.getAppointment()))
                .status(appointmentRecord.getStatus().name())
                .totalScore(appointmentRecord.getTotalScore())
                .studentCoopLevel(
                        appointmentRecord.getStudentCoopLevel() != null
                                ? appointmentRecord.getStudentCoopLevel().name()
                                : "UNKNOWN")
                .sessionFlow(
                        appointmentRecord.getSessionFlow() != null
                                ? appointmentRecord.getSessionFlow().name()
                                : "UNKNOWN")
                .noteSummary(appointmentRecord.getNoteSummary())
                .reason(appointmentRecord.getReason())
//                .answerRecords(appointmentRecord.getAnswerRecords()
//                        .stream()
//                        .map(answerRecordMapper::mapToAnswerRecordResponse)
//                        .toList())
                .build();
    }

}

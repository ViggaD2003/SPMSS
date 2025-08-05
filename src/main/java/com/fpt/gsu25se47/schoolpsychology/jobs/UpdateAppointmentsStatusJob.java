package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateAppointmentsStatusJob implements Job {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime cutoff = LocalDateTime.now();

        List<Appointment> appointments = appointmentRepository.findAllAppointmentExpired(AppointmentStatus.CONFIRMED, cutoff);

        appointments.stream()
                .filter(a -> a.getAssessmentScores().isEmpty())
                .forEach(a -> appointmentService.updateStatus(a.getId(), AppointmentStatus.ABSENT));
    }
}

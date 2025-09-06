package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateAppointmentsStatusJob implements Job {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

    @Transactional
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LocalDateTime cutoff = LocalDateTime.now();
        log.info("UpdateAppointmentsStatusJob started at [{}], cutoff time = [{}]", LocalDateTime.now(), cutoff);
        List<Appointment> appointments = appointmentRepository.findAllAppointmentExpired(AppointmentStatus.CONFIRMED, cutoff);
        log.info("Found [{}] expired appointments with status = CONFIRMED", appointments.size());
        appointments.stream()
                .filter(a -> a.getAssessmentScores().isEmpty())
                .forEach(a -> appointmentService.updateStatus(a.getId(), AppointmentStatus.ABSENT));
    }
}

package com.fpt.gsu25se47.schoolpsychology.service.crobjob;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCronjob {

    private final SurveyRepository surveyRepository;

    private final SlotRepository slotRepository;

    private final AppointmentRepository appointmentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateSurveyStatus() {
        LocalDate now = LocalDate.now();

        List<Survey> toPublish = surveyRepository.findByStartDateAndStatusDraft(now);
        toPublish.forEach(survey -> survey.setStatus(SurveyStatus.PUBLISHED));

        List<Survey> toFinish = surveyRepository.findByEndDateAndStatusPublished(now);
        toFinish.forEach(survey -> survey.setStatus(SurveyStatus.ARCHIVED));

        surveyRepository.saveAll(toPublish);
        surveyRepository.saveAll(toFinish);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void updateSlotStatus(){
        LocalDateTime now = LocalDateTime.now();

        List<Slot> toCompleted = slotRepository.findAllSlotsExpired(SlotStatus.PUBLISHED, now);
        toCompleted.forEach(slot -> slot.setStatus(SlotStatus.CLOSED));
        slotRepository.saveAll(toCompleted);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void updateAppointmentStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<Appointment> appointments = appointmentRepository.findAllAppointmentExpired(AppointmentStatus.CONFIRMED, now);
        appointments.forEach(appointment -> appointment.setStatus(AppointmentStatus.COMPLETED));
        appointmentRepository.saveAll(appointments);
    }

}

package com.fpt.gsu25se47.schoolpsychology.service.crobjob;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
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

    private final AppointmentService appointmentService;

    @Scheduled(cron = "0 0 */12 * * *")
    public void updateSurveyStatus() {
        LocalDate now = LocalDate.now();

        List<Survey> toPublish = surveyRepository.findByStartDateAndStatusDraft(now);
        toPublish.forEach(survey -> survey.setStatus(SurveyStatus.PUBLISHED));

        List<Survey> toFinish = surveyRepository.findByEndDateAndStatusPublished(now);
        toFinish.forEach(survey -> survey.setStatus(SurveyStatus.ARCHIVED));

        surveyRepository.saveAll(toPublish);
        surveyRepository.saveAll(toFinish);
    }

    @Scheduled(cron = "0 0 */12 * * *") // chạy mỗi ngày lúc 00:00
    public void processRecurringSurveys() {
        List<Survey> recurringSurveys = surveyRepository.findAllRecurringSurveys();

        LocalDate today = LocalDate.now();

        for (Survey survey : recurringSurveys) {
            if (shouldOpen(survey, today)) {
                survey.setStartDate(today);
                int numberDoSurvey = survey.getEndDate().getDayOfMonth() - survey.getStartDate().getDayOfMonth();
                survey.setEndDate(today.plusDays(numberDoSurvey));
                survey.setStatus(SurveyStatus.PUBLISHED);
                survey.setRound(survey.getRound() + 1);
                surveyRepository.save(survey);
                System.out.println("Recurring survey " + survey.getId() + " has been published");
            }
        }
    }

    @Scheduled(cron = "0 0 */12 * * *")
    public void updateAppointmentStatus() {
        LocalDateTime cutoff = LocalDateTime.now();

        List<Appointment> appointments = appointmentRepository.findAllAppointmentExpired(AppointmentStatus.CONFIRMED, cutoff);

        appointments.stream()
                .filter(a -> a.getAssessmentScores().isEmpty())
                .forEach(a -> appointmentService.updateStatus(a.getId(), AppointmentStatus.ABSENT));
    }

    @Scheduled(cron = "0 0 */12 * * *")
    public void updateSlotStatus() {
        LocalDateTime cutoff = LocalDateTime.now();

        List<Slot> slots = slotRepository.findAllSlotsExpired(cutoff, SlotStatus.PUBLISHED, SlotStatus.DRAFT);

        slots.forEach(s -> {
            s.setStatus(SlotStatus.CLOSED);
            slotRepository.save(s);
        });
    }

    private boolean shouldOpen(Survey survey, LocalDate today) {
        if (!survey.getIsRecurring()) return false;
        if (survey.getStartDate() == null) return true;

        return switch (survey.getRecurringCycle()) {
            case WEEKLY -> !survey.getStartDate().plusWeeks(1).isAfter(today);
            case MONTHLY -> !survey.getStartDate().plusMonths(1).isAfter(today);
            default -> false;
        };
    }
}


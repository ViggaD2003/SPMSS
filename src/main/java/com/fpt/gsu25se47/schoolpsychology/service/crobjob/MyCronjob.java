package com.fpt.gsu25se47.schoolpsychology.service.crobjob;

import com.fpt.gsu25se47.schoolpsychology.model.Survey;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;

import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCronjob {

    private final SurveyRepository surveyRepository;

//    private final SlotRepository slotRepository;
//
//    private final AppointmentRepository appointmentRepository;
//
//    private final AppointmentRecordRepository appointmentRecordRepository;

//    private final AppointmentRecordService appointmentRecordService;

    @Scheduled(cron = "0 0 */12 * * *")
    public void updateSurveyStatus() {
        LocalDate now = LocalDate.now();

        List<Survey> toPublish = surveyRepository.findByStartDateAndStatusDraft(now);
        toPublish.forEach(survey -> survey.setStatus(SurveyStatus.PUBLISHED));

        List<Survey> toFinish = surveyRepository.findByEndDateAndStatusPublished(now);
        toFinish.forEach(survey -> {
            survey.setStatus(SurveyStatus.ARCHIVED);
            survey.setIsUsed(Boolean.TRUE);
        });

        surveyRepository.saveAll(toPublish);
        surveyRepository.saveAll(toFinish);
    }

    @Scheduled(cron = "0 0 0 * * ?") // chạy mỗi ngày lúc 00:00
    public void processRecurringSurveys() {
        List<Survey> recurringSurveys = surveyRepository.findAllRecurringSurveys();

        LocalDate today = LocalDate.now();

        for (Survey survey : recurringSurveys) {
            if (shouldOpen(survey, today)) {
                survey.setStartDate(today);
                int numberDoSurvey = survey.getEndDate().getDayOfMonth() - survey.getStartDate().getDayOfMonth();
                survey.setEndDate(today.plusDays(numberDoSurvey));
                survey.setStatus(SurveyStatus.PUBLISHED);
                surveyRepository.save(survey);
                System.out.println("Recurring survey " + survey.getId() + " has been published");
            }
        }
    }

//    @Scheduled(cron = "0 */1 * * * *")
//    public void updateSlotStatus() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Slot> toCompleted = slotRepository.findAllSlotsExpired(now, SlotStatus.PUBLISHED, SlotStatus.DRAFT);
//        toCompleted.forEach(slot -> slot.setStatus(SlotStatus.CLOSED));
//        slotRepository.saveAll(toCompleted);
//    }
//
//    @Scheduled(cron = "0 */1 * * * *")
//    public void updateAppointmentStatus() {
//        LocalDateTime now = LocalDateTime.now();
//
//        List<Appointment> appointments = appointmentRepository.findAllAppointmentExpired(AppointmentStatus.CONFIRMED, now);
//        appointments.forEach(appointment -> {
//
//            if (!appointmentRecordRepository.existsByAppointmentId(appointment.getId())) {
//                appointment.setStatus(AppointmentStatus.EXPIRED);
//
//                CreateAppointmentRecordRequest request = new CreateAppointmentRecordRequest();
//                request.setAppointmentId(appointment.getId());
//                request.setStatus(RecordStatus.CANCELED);
//                request.setReason("Cuộc hẹn đã hết hiệu lực");
//                request.setReportCategoryRequests(Collections.emptyList());
//                appointmentRecordService.createAppointmentRecord(request);
//            } else {
//                appointment.setStatus(AppointmentStatus.COMPLETED);
//                List<AppointmentRecord> appointmentRecords = appointmentRecordRepository
//                        .findAllByAppointmentId(appointment.getId());
//                appointmentRecords.forEach(appointmentRecord -> {
//                    appointmentRecord.setStatus(RecordStatus.FINALIZED);
//                    appointmentRecordRepository.save(appointmentRecord);
//                });
//            }
//        });
//
//        appointmentRepository.saveAll(appointments);
//    }

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


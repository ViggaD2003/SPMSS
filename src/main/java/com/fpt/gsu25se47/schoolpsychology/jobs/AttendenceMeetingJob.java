package com.fpt.gsu25se47.schoolpsychology.jobs;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.common.GoogleTokenStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendenceMeetingJob implements Job {

    private final GoogleTokenStore tokenStore;
    private final AccountRepository accountRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        log.info("Running AttendenceMeetingJob...");

        try {
            String accessToken = tokenStore.getAccessToken();
            if (accessToken == null || accessToken.isBlank()) {
                log.warn("Access token for manager is not available. Skipping job.");
                return;
            }

            Calendar calendar = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new GoogleCredential().setAccessToken(accessToken)
            ).setApplicationName("School App").build();

            List<EventAttendee> attendees = accountRepository.findAll().stream()
                    .filter(e -> (e.getRole() == Role.STUDENT || e.getRole() == Role.TEACHER || e.getRole() == Role.COUNSELOR) && e.getEmail().contains("gmail.com"))
                    .map(acc -> new EventAttendee().setEmail(acc.getEmail()))
                    .collect(Collectors.toList());

            List<String> eventIds = accountRepository.findAll()
                    .stream()
                    .filter(acc -> acc.getRole() == Role.COUNSELOR || acc.getRole() == Role.TEACHER)
                    .map(acc -> {
                        if (acc.getRole() == Role.COUNSELOR && acc.getCounselor() != null) {
                            return acc.getCounselor().getEventId();
                        } else if (acc.getRole() == Role.TEACHER && acc.getTeacher() != null) {
                            return acc.getTeacher().getEventId();
                        }
                        return null;
                    })
                    .filter(eventId -> eventId != null && !eventId.isBlank()) // ✅ bỏ null/blank
                    .collect(Collectors.toList());

            for (String eventId : eventIds) {
                log.info("Updating attendees for event {}", eventId);

                // 4. Lấy event hiện tại
                Event event = calendar.events().get("primary", eventId).execute();

                // 5. Gán attendees mới
                event.setAttendees(attendees);

                // 6. Update lại event
                calendar.events().update("primary", eventId, event)
                        .setConferenceDataVersion(1)
                        .execute();
            }

            log.info("AttendenceMeetingJob finished successfully.");

        } catch (Exception e) {
            log.error("Error while updating meeting attendees", e);
        }
    }
}

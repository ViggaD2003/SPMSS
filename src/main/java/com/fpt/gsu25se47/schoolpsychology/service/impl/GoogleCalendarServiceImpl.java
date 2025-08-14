package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.common.GoogleTokenStore;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.GoogleCalendarService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    private final GoogleTokenStore tokenStore;

    private final AccountRepository accountRepository;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Override
    public Event createMeetLinkForTeacher(String teacherName, String roleName) {
        String accessToken = tokenStore.getAccessToken();

        try {
            return createEvent(teacherName,roleName, accessToken);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                // AccessToken hết hạn → dùng refresh_token để tạo mới
                try {
                    String newToken = refreshAccessToken();
                    tokenStore.updateAccessToken(newToken);
                    return createEvent(teacherName,roleName, newToken);
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to refresh token", ex);
                }
            }
            throw new RuntimeException("Failed to create Google Calendar event", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    private Event createEvent(String teacherName, String roleName, String accessToken) throws IOException, GeneralSecurityException {
        Calendar calendar = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new GoogleCredential().setAccessToken(accessToken)
        ).setApplicationName("School App").build();

        // Lấy danh sách email và chuyển sang EventAttendee
        List<EventAttendee> attendees = accountRepository.findAll().stream()
                .filter(e -> e.getRole() == Role.STUDENT && e.getEmail().contains("gmail.com"))
                .map(account -> new EventAttendee().setEmail(account.getEmail()))
                .collect(Collectors.toList());

        Event event = new Event()
                .setSummary(roleName.equals("TEACHER") ? "Phòng họp giáo viên: " + teacherName : "Phòng họp chuyên viên " + teacherName)
                .setStart(new EventDateTime().setDateTime(new DateTime(System.currentTimeMillis())))
                .setEnd(new EventDateTime().setDateTime(new DateTime(System.currentTimeMillis() + 15 * 60 * 1000)))
                .setConferenceData(new ConferenceData().setCreateRequest(
                        new CreateConferenceRequest()
                                .setRequestId(UUID.randomUUID().toString())
                                .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet"))
                ))
                .setAttendees(attendees); // ✅ đúng kiểu rồi

        return calendar.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();
    }

    private String refreshAccessToken() throws IOException, GeneralSecurityException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        TokenResponse response = new GoogleRefreshTokenRequest(
                transport,
                jsonFactory,
                tokenStore.getRefreshToken(),
                clientId,
                clientSecret
        ).execute();

        return response.getAccessToken();
    }
}

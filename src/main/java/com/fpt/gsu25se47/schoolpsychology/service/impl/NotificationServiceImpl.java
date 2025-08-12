package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiSettingRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.NotificationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Notifications;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.NotificationRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final AccountRepository accountRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final AppointmentRepository appointmentRepository;


    @Override
    public NotiResponse saveNotification(NotiRequest request) {
        Notifications notification = notificationMapper.mapNotification(request);

        Account account = accountRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Not found account !"));

        notification.setReceiver(account);
        notification.setIsRead(false);

        return notificationMapper.mapToResponse(notificationRepository.save(notification));
    }

    @Override
    public List<NotiResponse> getAllNotificationsByAccountId(Integer accountId) {
        List<Notifications> notifications = notificationRepository.findAllByReceiverId(accountId);
        return notifications.stream()
                .map(notificationMapper::mapToResponse)
                .toList();
    }

    @Override
    public void sendNotification(String username, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(
                username, // username của user nhận
                destination,
                payload
        );
    }

    @Override
    public void sendNotificationSetting(NotiSettingRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getEntityId())
                .orElseThrow(() -> new RuntimeException("Not found appointment !"));

        if (request.getNotifyParent()) {
            if (appointment.getBookedFor().getRole() == Role.STUDENT) {
                appointment.getBookedFor().getStudent().getRelationships().forEach(relationship -> {
                    NotiRequest notiRequest = NotiRequest
                            .builder()
                            .title("New Appointment Created !")
                            .notificationType("APPOINTMENT")
                            .relatedEntityId(request.getEntityId())
                            .content("Your children already have an appointment !")
                            .username(relationship.getGuardian().getAccount().getEmail())
                            .build();
                    this.sendNotification(relationship.getGuardian().getAccount().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
                });
            } else {
                NotiRequest notiRequest = NotiRequest
                        .builder()
                        .title("New Appointment Created !")
                        .notificationType("APPOINTMENT")
                        .relatedEntityId(request.getEntityId())
                        .content("Your children already have an appointment !")
                        .username(appointment.getBookedFor().getEmail())
                        .build();
                this.sendNotification(appointment.getBookedFor().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
            }
        }

        if (request.getNotifyTeacher() || request.getNotifyCounselor()) {
            if(appointment.getSlot().getHostedBy().getRole() == Role.TEACHER) {
                NotiRequest notiRequest = NotiRequest
                        .builder()
                        .title("New Appointment Created !")
                        .notificationType("APPOINTMENT")
                        .relatedEntityId(request.getEntityId())
                        .content("Your student already have an appointment !")
                        .username(appointment.getSlot().getHostedBy().getEmail())
                        .build();
                this.sendNotification(appointment.getSlot().getHostedBy().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
            } else {
                NotiRequest notiRequest = NotiRequest
                        .builder()
                        .title("New Appointment Created !")
                        .notificationType("APPOINTMENT")
                        .relatedEntityId(request.getEntityId())
                        .content("You have appointment with " + appointment.getBookedFor().getFullName())
                        .username(appointment.getSlot().getHostedBy().getEmail())
                        .build();
                this.sendNotification(appointment.getSlot().getHostedBy().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
            }
        }

        if(request.getNotifyManager()){
            NotiRequest notiRequest = NotiRequest
                    .builder()
                    .title("New Appointment Created !")
                    .notificationType("APPOINTMENT")
                    .relatedEntityId(request.getEntityId())
                    .content("System have new appointment")
                    .username("danhkvtse172932@fpt.edu.vn")
                    .build();
            this.sendNotification("danhkvtse172932@fpt.edu.vn","/queue/notifications", this.saveNotification(notiRequest));
        }
    }
}

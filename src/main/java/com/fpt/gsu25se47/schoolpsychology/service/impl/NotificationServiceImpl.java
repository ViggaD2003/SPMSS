package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiSettingRequest;
import com.fpt.gsu25se47.schoolpsychology.mapper.NotificationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Notifications;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.NotificationRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import com.fpt.gsu25se47.schoolpsychology.utils.BuildNotiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final AccountRepository accountRepository;

    private final SimpMessagingTemplate messagingTemplate;
    private final AppointmentRepository appointmentRepository;
    private final ClassRepository classRepository;


    @Override
    public NotiResponse saveNotification(NotiRequest request) {
        Notifications notification = notificationMapper.mapNotification(request);

        Account account = accountRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Not found account !"));

        notification.setNotificationType(request.getNotificationType().contains("_") ? request.getNotificationType() : request.getNotificationType() + "_INFO");
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
    public void readMessage(UUID notificationId) {
        Notifications notifications = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Not found notification !"));

        notifications.setIsRead(true);
        notificationRepository.save(notifications);
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
            appointment.getBookedFor().getStudent().getRelationships().forEach(relationship -> {
                NotiRequest notiRequest = BuildNotiRequest.buildNotiRequest(request.getEntityId(), request.getTitle(), request.getContent(),
                        request.getNotificationType(),
                        relationship.getGuardian().getAccount().getEmail());
                this.sendNotification(relationship.getGuardian().getAccount().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
            });
        }

        if(request.getNotifyTeacher()) {
            Classes activeClass = classRepository.findActiveClassByStudentId(appointment.getBookedFor().getId());
            NotiRequest notiRequest = BuildNotiRequest.buildNotiRequest(request.getEntityId(),
                    request.getTitle(),
                    request.getContent(),
                    request.getNotificationType(),
                    activeClass.getTeacher().getAccount().getEmail());

                this.sendNotification(activeClass.getTeacher().getAccount().getEmail(),
                        "/queue/notifications",
                        this.saveNotification(notiRequest));
        }

        if(request.getNotifyCounselor()){
            NotiRequest notiRequest = BuildNotiRequest.buildNotiRequest(request.getEntityId(), request.getTitle(), request.getContent(), request.getNotificationType(), appointment.getSlot().getHostedBy().getEmail());
                this.sendNotification(appointment.getSlot().getHostedBy().getEmail(),"/queue/notifications", this.saveNotification(notiRequest));
        }
    }



}

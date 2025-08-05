package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.NotiRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.NotiResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.NotificationMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Notifications;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
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


    @Override
    public NotiResponse saveNotification(NotiRequest request) {
        Notifications notification = notificationMapper.mapNotification(request);

        Account account = accountRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Not found account !"));

        notification.setReceiver(account);
        notification.setIsRead(false);

        Notifications entity = notificationRepository.save(notification);
        return notificationMapper.mapToResponse(entity);
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


}

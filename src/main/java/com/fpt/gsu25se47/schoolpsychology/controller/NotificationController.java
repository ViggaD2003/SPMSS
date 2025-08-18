package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.service.inter.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getNotifications(@PathVariable("accountId") Integer accountId) {
        return ResponseEntity.ok(notificationService.getAllNotificationsByAccountId(accountId).reversed());
    }

    @PatchMapping("/read/{notiId}")
    public ResponseEntity<String> readMessages(@PathVariable("notiId") UUID notiId) {
        notificationService.readMessage(notiId);
        return ResponseEntity.status(HttpStatus.OK).body("Read");
    }
}

package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;


    @PostMapping
    public String sendMessage(@RequestBody ChatRequest chatRequest) {
        return chatService.sendMessage(chatRequest);
    }


    @GetMapping("/chat-room")
    public ResponseEntity<Optional<?>> findChatRoom(@RequestParam Integer caseId) {
        return ResponseEntity.ok(chatService.getAllChatRooms(caseId));
    }

    @GetMapping("/chat-message")
    public ResponseEntity<List<ChatMessageDto>> findAllChatMessage(@RequestParam(name = "chatRoomId") Integer chatRoomId) {
        return ResponseEntity.ok(chatService.getAllChatMessages(chatRoomId));
    }
}

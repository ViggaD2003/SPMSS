package com.fpt.gsu25se47.schoolpsychology.controller;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.AiChatResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionResponse;
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
    public ResponseEntity<AiChatResponse> sendMessage(@RequestBody ChatRequest chatRequest) {
        return ResponseEntity.ok(chatService.sendMessage(chatRequest));
    }


    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        return ResponseEntity.ok(chatService.getAllSessionResponses());
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<SessionDetailResponse> getSessionDetail(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatService.getSessionDetail(sessionId));
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

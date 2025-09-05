package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.AiChatResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.MessageResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.ai.AiSession;
import com.fpt.gsu25se47.schoolpsychology.model.ai.SessionConversation;
import com.fpt.gsu25se47.schoolpsychology.repository.AiSessionRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SessionConversationRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AIChatService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.utils.RolePromptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AIChatServiceImpl implements AIChatService {

    private final AccountService accountService;
    private final ChatClient chatClient;
    private final AiSessionRepository aiSessionRepository;
    private final SessionConversationRepository sessionConversationRepository;
    private final RolePromptUtils rolePromptUtils;

    @Override
    public AiChatResponse sendMessage(ChatRequest request) {
        Account account = accountService.getCurrentAccount();

        // Find or create a session
        AiSession session = aiSessionRepository.findById(request.getSessionId() != null ? request.getSessionId() : "")
                .orElseGet(() -> aiSessionRepository.save(
                        AiSession.builder()
                                .user(account)
                                .name("Chat session for " + account.getUsername())
                                .build()
                ));

        // Ensure conversation exists or create a new one
        String conversationId = request.getConversationId() != null
                ? request.getConversationId()
                : UUID.randomUUID().toString();

        if (!sessionConversationRepository.existsByConversationId(conversationId)) {
            sessionConversationRepository.save(SessionConversation.builder()
                    .conversationId(conversationId)
                    .session(session)
                    .build()
            );
        }
        // Save user message into memory

        return AiChatResponse.builder()
                .aiMessage(chatClient.prompt()
                        .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                        .user(request.getMessage())
                        .system(a -> {
                            a.param("communicationStyle", rolePromptUtils.buildCommunicationStyle(account.getRole()));
                            a.param("ragContext", "");
                            a.param("ragInstructions", "");
                            a.param("currentUserRole", account.getRole());
                            a.param("accountId", account.getId());
                        })
                        .call()
                        .content())
                .userMessage(request.getMessage())
                .sessionId(session.getId())
                .conversationId(conversationId)
                .build();
    }

    @Override
    public List<SessionResponse> getAllSessionResponses() {

        List<AiSession> aiSessions = aiSessionRepository.findAll();

        return aiSessions.stream()
                .map(as -> SessionResponse.builder()
                        .id(as.getId())
                        .createdTime(as.getCreatedTime())
                        .editedTime(as.getEditedTime())
                        .userId(as.getUser().getId())
                        .name(as.getName())
                        .build())
                .toList();
    }

    @Override
    public SessionDetailResponse getSessionDetail(String sessionId) {
        AiSession aiSession = aiSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "This session not found for ID: " + sessionId));

        List<MessageResponse> messageResponses = sessionConversationRepository.getAllMessagesBySessionId(aiSession.getId())
                .stream()
                .map(row -> new MessageResponse(
                        (String) row[0],
                        (String) row[1],
                        (String) row[2],
                        ((Timestamp) row[3]).toLocalDateTime()
                ))
                .toList();

        return SessionDetailResponse.builder()
                .id(aiSession.getId())
                .userId(aiSession.getUser().getId())
                .createdTime(aiSession.getCreatedTime())
                .editedTime(aiSession.getEditedTime())
                .name(aiSession.getName())
                .messageResponses(messageResponses)
                .build();
    }

}

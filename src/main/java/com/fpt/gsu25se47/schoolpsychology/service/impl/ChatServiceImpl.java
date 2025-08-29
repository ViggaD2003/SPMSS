package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.AiChatResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.MessageResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatRoomResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ChatMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.ChatMessage;
import com.fpt.gsu25se47.schoolpsychology.model.ChatRoom;
import com.fpt.gsu25se47.schoolpsychology.model.ai.AiSession;
import com.fpt.gsu25se47.schoolpsychology.model.ai.SessionConversation;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import com.fpt.gsu25se47.schoolpsychology.utils.RolePromptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final AccountService accountService;
    private final AiSessionRepository aiSessionRepository;
    private final SessionConversationRepository sessionConversationRepository;
    private final RolePromptUtils rolePromptUtils;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CaseRepository caseRepository;

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
                            a.param("dataAccessRules", rolePromptUtils.buildDataAccessRules(account.getRole()));
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

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto, Integer chatRoomId) {
        ChatMessage chatMessage = ChatMessage.builder()
                .createdAt(LocalDateTime.now())
                .email(chatMessageDto.getSender())
                .content(chatMessageDto.getMessage())
                .chatRoom(chatRoomRepository.findById(chatRoomId)
                        .orElseThrow(() -> new RuntimeException("Room not found"))
                )
                .build();
        System.out.println("ENTITY" + chatMessage);
        chatMessageRepository.save(chatMessage);

        return chatMessageDto;
    }

    @Override
    public void createChatRoom(Integer caseId) {
        Cases cases = caseRepository.findById(caseId).orElseThrow(() -> new RuntimeException("caseId not found"));
        if (cases.getStudent().getRole() == Role.STUDENT) {
            chatRoomRepository.save(ChatRoom.builder()
                    .cases(cases)
                    .email(cases.getStudent().getEmail())
                    .roleRoom(cases.getStudent().getRole().toString())
                    .timeStamp(LocalDateTime.now())
                    .build());
        }

        cases.getStudent().getStudent().getRelationships().forEach(relationship -> {
            chatRoomRepository.save(ChatRoom.builder()
                    .cases(cases)
                    .email(relationship.getGuardian().getAccount().getEmail())
                    .roleRoom(relationship.getGuardian().getAccount().getRole().toString())
                    .timeStamp(LocalDateTime.now())
                    .build());
        });
    }

    @Override
    public Optional<?> getAllChatRooms(Integer caseId) {
        Account account = accountService.getCurrentAccount();

        if (account.getRole() == Role.COUNSELOR) {
            return Optional.of(chatRoomRepository.findAllByCasesId(caseId)
                    .stream().map(this::mapToChatRoomResponse).collect(Collectors.toList()));
        } else {
            return Optional.of(this.mapToChatRoomResponse(chatRoomRepository.findAllByCasesIdAndByRoleRoom(caseId, account.getRole().toString(), account.getEmail())));
        }
    }

    @Override
    public List<ChatMessageDto> getAllChatMessages(Integer chatRoomId) {
        List<ChatMessage> list = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return list.stream().map(chatMapper::toChatMessageDto).collect(Collectors.toList());
    }

    private ChatRoomResponse mapToChatRoomResponse(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .email(chatRoom.getEmail())
                .roleRoom(chatRoom.getRoleRoom())
                .timeStamp(chatRoom.getTimeStamp())
                .build();
    }
}

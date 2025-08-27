package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.ChatMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.model.ChatMessage;
import com.fpt.gsu25se47.schoolpsychology.model.ChatRoom;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ChatMessageRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ChatRoomRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

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
    @Autowired
    private AccountService accountService;

    public ChatServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private boolean isRelevantToSchoolPsychology(String userMessage) {
        String checkPrompt = String.format(
                "Câu hỏi sau có liên quan đến tâm lý học đường không? Trả lời duy nhất: CÓ hoặc KHÔNG.\nCâu hỏi: \"%s\"",
                userMessage
        );

        String response = chatClient.prompt(checkPrompt)
                .call()
                .content()
                .trim()
                .toUpperCase();

        return response.contains("CÓ");
    }

    @Override
    public String sendMessage(ChatRequest request) {
        String userMessage = request.getMessage();
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
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
                    .stream().map(item -> item.getId()).collect(Collectors.toList()));
        } else {
            return Optional.of(chatRoomRepository.findAllByCasesIdAndByRoleRoom(caseId, account.getRole().toString(), account.getEmail()));
        }
    }

    @Override
    public List<ChatMessageDto> getAllChatMessages(Integer chatRoomId) {
        List<ChatMessage> list = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return list.stream().map(chatMapper::toChatMessageDto).collect(Collectors.toList());
    }
}

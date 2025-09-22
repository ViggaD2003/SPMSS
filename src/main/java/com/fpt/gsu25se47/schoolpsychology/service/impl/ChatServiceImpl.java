package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatRoomResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final AccountService accountService;

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
    public void createChatRoom(Integer caseId, Boolean notify) {
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

package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.ChatMapper;
import com.fpt.gsu25se47.schoolpsychology.model.ChatMessage;
import com.fpt.gsu25se47.schoolpsychology.model.ChatRoom;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ChatMessageRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ChatRoomRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        ChatMessage chatMessage = chatMapper.toChatMessage(chatMessageDto);
        chatMessage.setSender(accountRepository.save(chatMessage.getSender()));
        chatMessage.setChatRoom(chatRoomRepository.findById(chatRoomId).
                orElseThrow(() -> new RuntimeException("chatRoom not found")));

        return chatMapper.toChatMessageDto(chatMessageRepository.save(chatMessage));
    }

    @Override
    public void createChatRoom(Integer caseId) {
        for(int i = 0; i < 2; i++){
            chatRoomRepository.save(ChatRoom.builder()
                    .cases(caseRepository.findById(caseId).orElseThrow(() -> new RuntimeException("caseId not found")))
                    .timeStamp(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public List<Integer> getAllChatRooms(Integer caseId) {
        List<Integer> roomIds = chatRoomRepository.findAllByCasesId(caseId)
                .stream().map(item -> item.getId()).collect(Collectors.toList());
        return roomIds;
    }
}

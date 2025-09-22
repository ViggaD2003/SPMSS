package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    ChatMessageDto saveMessage(ChatMessageDto chatMessageDto, Integer chatRoomId);

    void createChatRoom(Integer caseId, Boolean notify);

    Optional<?> getAllChatRooms(Integer caseId);

    List<ChatMessageDto> getAllChatMessages(Integer caseId);
}

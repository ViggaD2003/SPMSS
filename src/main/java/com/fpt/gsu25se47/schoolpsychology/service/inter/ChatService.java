package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.AiChatResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ChatMessageDto;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    AiChatResponse sendMessage(ChatRequest request);
    List<SessionResponse> getAllSessionResponses();
    SessionDetailResponse getSessionDetail(String sessionId);

    ChatMessageDto saveMessage(ChatMessageDto chatMessageDto, Integer chatRoomId);

    void createChatRoom(Integer caseId);

    Optional<?> getAllChatRooms(Integer caseId);

    List<ChatMessageDto> getAllChatMessages(Integer caseId);
}

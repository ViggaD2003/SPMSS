package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.AiChatResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Ai.SessionResponse;

import java.util.List;

public interface AIChatService {
    AiChatResponse sendMessage(ChatRequest request);
    List<SessionResponse> getAllSessionResponses();
    SessionDetailResponse getSessionDetail(String sessionId);
}

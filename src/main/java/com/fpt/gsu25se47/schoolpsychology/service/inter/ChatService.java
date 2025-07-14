package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;

public interface ChatService {

    String sendMessage(ChatRequest request);
}

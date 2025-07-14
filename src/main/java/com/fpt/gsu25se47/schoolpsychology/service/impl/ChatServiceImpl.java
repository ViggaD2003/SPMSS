package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ChatRequest;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String sendMessage(ChatRequest request) {
        return chatClient.prompt(request.getMessage())
                .call()
                .content();
    }
}

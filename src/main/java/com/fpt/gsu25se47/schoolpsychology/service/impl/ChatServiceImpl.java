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

        if (!isRelevantToSchoolPsychology(userMessage)) {
            return "Sorry, the system only supports questions related to school psychology.";
        }

        return chatClient.prompt(userMessage)
                .call()
                .content();
    }
}

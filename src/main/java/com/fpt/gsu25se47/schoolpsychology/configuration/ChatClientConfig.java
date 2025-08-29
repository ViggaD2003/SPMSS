package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.advisors.TokenUsageAuditAdvisor;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.tools.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final TeacherTools teacherTools;
    private final AppointmentTools appointmentTools;
    private final SupportProgramTools supportProgramTools;
    private final DashboardTools dashboardTools;
    private final AccountTools accountTools;

    private final Map<String, String> promptTemplateCache = new ConcurrentHashMap<>();

    private final AccountService accountService;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        SimpleLoggerAdvisor simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        TokenUsageAuditAdvisor tokenUsageAuditAdvisor = new TokenUsageAuditAdvisor();

        return chatClientBuilder
                .defaultAdvisors(List.of(simpleLoggerAdvisor, tokenUsageAuditAdvisor, messageChatMemoryAdvisor))
                .defaultTools(teacherTools, appointmentTools, supportProgramTools, accountTools, dashboardTools)
                .defaultSystem(systemPromptTemplate)
                .build();
    }

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(15)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }


}

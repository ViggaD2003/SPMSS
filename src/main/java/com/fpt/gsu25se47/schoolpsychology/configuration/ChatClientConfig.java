package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.fpt.gsu25se47.schoolpsychology.advisors.TokenUsageAuditAdvisor;
import com.fpt.gsu25se47.schoolpsychology.tools.AppointmentTools;
import com.fpt.gsu25se47.schoolpsychology.tools.SupportProgramTools;
import com.fpt.gsu25se47.schoolpsychology.tools.TeacherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class ChatClientConfig {

    private final TeacherTools teacherTools;
    private final AppointmentTools appointmentTools;
    private final SupportProgramTools supportProgramTools;

    public ChatClientConfig(TeacherTools teacherTools, AppointmentTools appointmentTools, SupportProgramTools supportProgramTools) {
        this.teacherTools = teacherTools;
        this.appointmentTools = appointmentTools;
        this.supportProgramTools = supportProgramTools;
    }

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                .defaultTools(teacherTools, appointmentTools, supportProgramTools)
                .defaultSystem(systemPromptTemplate)
                .build();
    }
}

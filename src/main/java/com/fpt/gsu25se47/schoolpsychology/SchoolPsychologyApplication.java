package com.fpt.gsu25se47.schoolpsychology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolPsychologyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolPsychologyApplication.class, args);
    }
}

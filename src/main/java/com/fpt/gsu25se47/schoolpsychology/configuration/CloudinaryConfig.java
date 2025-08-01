package com.fpt.gsu25se47.schoolpsychology.configuration;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name}")
    private String CLOUD_NAME;

    @Value("${cloudinary.api_key}")
    private String API_KEY;

    @Value("${cloudinary.api_secret}")
    private String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("secret_key", API_SECRET);
        return new Cloudinary(config);
    }
}

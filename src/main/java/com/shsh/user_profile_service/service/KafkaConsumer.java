package com.shsh.user_profile_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shsh.user_profile_service.model.UserProfile;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_registration", groupId = "profile-service-group")
    public void consume(String message) {
        log.info("Consumed message: {}", message);

        try {
            UserRegistrationEvent event = objectMapper.readValue(message, UserRegistrationEvent.class);

            UserProfile userProfile = new UserProfile();
            userProfile.setId(event.getUserId());
            userProfile.setUsername(event.getUsername());
            userProfile.setEmail(event.getEmail());
            userProfile.setDescriptionOfProfile("New user profile");
            userProfile.setStatus("Active");

            userProfileService.save(userProfile);
        } catch (IOException e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
    @Data
    static class UserRegistrationEvent {
        private UUID userId;
        private String username;
        private String email;;

    }
}
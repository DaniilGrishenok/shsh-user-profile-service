package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserProfileService userProfileService;

    // Поиск пользователей по подстроке в username
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<UserProfile> users = userProfileService.findUsersByUsernameSubstring(query);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Пользователи не найдены"));
        }
        return ResponseEntity.ok(users);
    }

    // Получение ID пользователя по username
    @GetMapping("/id-by-username")
    public ResponseEntity<?> getIdByUsername(@RequestParam String username) {
        try {
            String userId = userProfileService.getIdByUsername(username);
            return ResponseEntity.ok(new IdResponse(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        }
    }


    private record IdResponse(String id) {}

    // DTO для ошибок
    private record ErrorResponse(String message) {}
}
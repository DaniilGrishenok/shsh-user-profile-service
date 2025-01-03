package com.shsh.user_profile_service.controller;

import com.shsh.user_profile_service.dto.GetStatusResponse;
import com.shsh.user_profile_service.dto.UserStatus;
import com.shsh.user_profile_service.model.UserProfile;
import com.shsh.user_profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ups/api")
@RequiredArgsConstructor
public class UserController {
    private final UserProfileService userProfileService;
    /*
    *
    *
    *
    * */

    @GetMapping("/users/{userId}/status")
    public ResponseEntity<GetStatusResponse> getStatus(@PathVariable String userId) {
        UserStatus userStatus = userProfileService.getStatus(userId);
        GetStatusResponse response = new GetStatusResponse(userId,userStatus.getStatus(),userStatus.getLastSeen() );

        return ResponseEntity.ok(response);
    }
    @PostMapping("/users/statuses")
    public ResponseEntity<List<GetStatusResponse>> getStatuses(@RequestBody List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<UserStatus> statuses = userProfileService.getStatuses(userIds);
        List<GetStatusResponse> response = statuses.stream()
                .map(status -> new GetStatusResponse(status.getUserId(), status.getStatus(), status.getLastSeen()))
                .toList();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        List<UserProfile> users = userProfileService.findUsersByUsernameSubstring(query);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Пользователи не найдены"));
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{userId}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String userId) {
        boolean exists = userProfileService.existsById(userId);
        return ResponseEntity.ok(exists);
    }

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

    private record ErrorResponse(String message) {}
}
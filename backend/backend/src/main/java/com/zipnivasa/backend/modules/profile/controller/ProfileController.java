package com.zipnivasa.backend.modules.profile.controller;

import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.config.JwtAuthenticationFilter;
import com.zipnivasa.backend.modules.profile.dto.ProfileUpdateRequest;
import com.zipnivasa.backend.modules.profile.service.ProfileService;
import com.zipnivasa.backend.modules.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @GetMapping("/me")
    public ResponseEntity<?> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        String userId = principal.getId();
        String role = principal.getRole();

        User user = profileService.getUser(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(ApiResponse.failure("User not found"));
        }

        Object roleData = profileService.getRoleData(role, userId);

        return ResponseEntity.ok(
                new Object() {
                    public final boolean success = true;
                    public final User userInfo = user;
                    public final Object roleDataInfo = roleData;
                }
        );
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        String userId = principal.getId();
        String role = principal.getRole();

        profileService.updateProfile(userId, role, req);

        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", null));
    }
}

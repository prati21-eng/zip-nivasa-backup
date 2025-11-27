package com.zipnivasa.backend.modules.auth.controller;

import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.config.JwtAuthenticationFilter;
import com.zipnivasa.backend.modules.auth.dto.AuthResponse;
import com.zipnivasa.backend.modules.auth.dto.LoginRequest;
import com.zipnivasa.backend.modules.auth.dto.RegisterRequest;
import com.zipnivasa.backend.modules.auth.service.AuthService;
import com.zipnivasa.backend.modules.user.model.*;
import com.zipnivasa.backend.modules.user.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PGOwnerRepository pgOwnerRepository;
    private final MessOwnerRepository messOwnerRepository;
    private final LaundryOwnerRepository laundryOwnerRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest req) {
        try {
            authService.register(req);
            return ResponseEntity.ok(ApiResponse.success("Registration successful", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.failure("Server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest req) {
        try {
            AuthResponse response = authService.login(req);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.ok(ApiResponse.failure(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ApiResponse.failure("Server error"));
        }
    }

    // GET /api/auth/me  (similar to getMe in Node)
    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        String userId = principal.getId();

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(ApiResponse.failure("User not found"));
        }

        Object roleData = null;
        switch (user.getRole()) {
            case "tenant" -> roleData = tenantRepository.findById(user.getRoleId()).orElse(null);
            case "pgowner" -> roleData = pgOwnerRepository.findById(user.getRoleId()).orElse(null);
            case "messowner" -> roleData = messOwnerRepository.findById(user.getRoleId()).orElse(null);
            case "laundry" -> roleData = laundryOwnerRepository.findById(user.getRoleId()).orElse(null);
        }

        MeResponse dto = new MeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                roleData
        );

        return ResponseEntity.ok(
                ApiResponse.success("User fetched", dto)
        );
    }

    // GET /api/auth/user/:id  (getUserPublic)
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserPublic(@PathVariable String id) {
        return userRepository.findById(id)
                .map(u -> {
                    PublicUserResponse dto = new PublicUserResponse(
                            u.getId(),
                            u.getName(),
                            u.getPhone(),
                            u.getRole()
                    );
                    return ResponseEntity.ok(ApiResponse.success("User found", dto));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.failure("User not found")));
    }


    @Data
    @AllArgsConstructor
    static class MeResponse {
        private String id;
        private String name;
        private String email;
        private String role;
        private Object profile;
    }

    @Data
    @AllArgsConstructor
    static class PublicUserResponse {
        private String id;
        private String name;
        private String phone;
        private String role;
    }
}

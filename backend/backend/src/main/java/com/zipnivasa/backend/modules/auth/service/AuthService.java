package com.zipnivasa.backend.modules.auth.service;

import com.zipnivasa.backend.config.JwtUtil;
import com.zipnivasa.backend.modules.auth.dto.AuthResponse;
import com.zipnivasa.backend.modules.auth.dto.LoginRequest;
import com.zipnivasa.backend.modules.auth.dto.RegisterRequest;
import com.zipnivasa.backend.modules.user.model.*;
import com.zipnivasa.backend.modules.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PGOwnerRepository pgOwnerRepository;
    private final MessOwnerRepository messOwnerRepository;
    private final LaundryOwnerRepository laundryOwnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest req) {

        if (req.getName() == null || req.getEmail() == null ||
                req.getPhone() == null || req.getPassword() == null ||
                req.getRole() == null) {
            throw new IllegalArgumentException("name, email, phone, password and role are required");
        }

        String email = req.getEmail().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String role = req.getRole().toLowerCase(Locale.ROOT);
        String roleModelName = null;
        String roleDocId = null;

        switch (role) {
            case "tenant" -> {
                if (req.getProfessionType() == null ||
                        (!"student".equalsIgnoreCase(req.getProfessionType())
                                && !"job".equalsIgnoreCase(req.getProfessionType()))) {
                    throw new IllegalArgumentException("professionType must be 'student' or 'job'");
                }

                Tenant tenant = Tenant.builder()
                        .professionType(req.getProfessionType().toLowerCase())
                        .collegeName(req.getCollegeName())
                        .course(req.getCourse())
                        .year(req.getYear())
                        .companyName(req.getCompanyName())
                        .workLocation(req.getWorkLocation())
                        .jobRole(req.getJobRole())
                        .city(req.getCity())
                        .build();

                tenant = tenantRepository.save(tenant);
                roleModelName = "Tenant";
                roleDocId = tenant.getId();
            }
            case "pgowner" -> {
                PGOwner pgOwner = PGOwner.builder()
                        .pgName(req.getPgName())
                        .pgLocation(req.getPgLocation())
                        .pgCapacity(req.getPgCapacity())
                        .pgFacilities(req.getPgFacilities())
                        .build();

                pgOwner = pgOwnerRepository.save(pgOwner);
                roleModelName = "PGOwner";
                roleDocId = pgOwner.getId();
            }
            case "messowner" -> {
                MessOwner messOwner = MessOwner.builder()
                        .messName(req.getMessName())
                        .messLocation(req.getMessLocation())
                        .messCapacity(req.getMessCapacity())
                        .messType(req.getMessType())
                        .build();

                messOwner = messOwnerRepository.save(messOwner);
                roleModelName = "MessOwner";
                roleDocId = messOwner.getId();
            }
            case "laundry" -> {
                LaundryOwner laundryOwner = LaundryOwner.builder()
                        .laundryName(req.getLaundryName())
                        .serviceLocation(req.getServiceLocation())
                        .ratePerKg(req.getRatePerKg())
                        .build();

                laundryOwner = laundryOwnerRepository.save(laundryOwner);
                roleModelName = "LaundryOwner";
                roleDocId = laundryOwner.getId();
            }
            default -> throw new IllegalArgumentException("Invalid role");
        }

        User user = User.builder()
                .name(req.getName())
                .email(email)
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .roleId(roleDocId)
                .roleModel(roleModelName)
                .build();

        user = userRepository.save(user);

        switch (role) {
            case "tenant" -> {
                Tenant tenant = tenantRepository.findById(roleDocId).orElse(null);
                if (tenant != null) {
                    tenant.setUserId(user.getId());
                    tenantRepository.save(tenant);
                }
            }
            case "pgowner" -> {
                PGOwner pgOwner = pgOwnerRepository.findById(roleDocId).orElse(null);
                if (pgOwner != null) {
                    pgOwner.setUserId(user.getId());
                    pgOwnerRepository.save(pgOwner);
                }
            }
            case "messowner" -> {
                MessOwner messOwner = messOwnerRepository.findById(roleDocId).orElse(null);
                if (messOwner != null) {
                    messOwner.setUserId(user.getId());
                    messOwnerRepository.save(messOwner);
                }
            }
            case "laundry" -> {
                LaundryOwner laundryOwner = laundryOwnerRepository.findById(roleDocId).orElse(null);
                if (laundryOwner != null) {
                    laundryOwner.setUserId(user.getId());
                    laundryOwnerRepository.save(laundryOwner);
                }
            }
        }
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        AuthResponse.UserInfo info = new AuthResponse.UserInfo(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(token, info);
    }
}

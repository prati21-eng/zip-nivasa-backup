package com.zipnivasa.backend.modules.pg.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipnivasa.backend.common.file.FileStorageService;
import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.config.JwtAuthenticationFilter;
import com.zipnivasa.backend.modules.pg.dto.PGRequest;
import com.zipnivasa.backend.modules.pg.model.PG;
import com.zipnivasa.backend.modules.pg.service.PGService;
import com.zipnivasa.backend.modules.user.model.User;
import com.zipnivasa.backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pgs")
@RequiredArgsConstructor
public class PGController {

    private final PGService pgService;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET /api/pgs
     * Like Node: returns array of PGs (not wrapped).
     */
    @GetMapping
    public ResponseEntity<List<PG>> getAllPGs() {
        List<PG> list = pgService.getAllPGs();
        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/pgs/{id}
     * Node version:
     *  - finds PG by id
     *  - populates owner with name, email, phone, role
     *  - returns { success, pg, ownerDetails }
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPGById(@PathVariable String id) {
        PG pg = pgService.getPGById(id);
        if (pg == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.failure("PG not found"));
        }

        Optional<User> ownerOpt = userRepository.findById(pg.getOwner());
        Map<String, Object> ownerDetails = ownerOpt
                .map(u -> Map.<String, Object>of(
                        "id", u.getId(),
                        "name", u.getName(),
                        "email", u.getEmail(),
                        "phone", u.getPhone(),
                        "role", u.getRole()
                ))
                .orElse(Collections.emptyMap());

        // Mimic your Node response
        return ResponseEntity.ok(Map.of(
                "success", true,
                "pg", pg,
                "ownerDetails", ownerDetails
        ));
    }

    /**
     * GET /api/pgs/owner/list
     * Requires JWT (protect)
     * Node version: uses req.user.id
     */
    @GetMapping("/owner/list")
    public ResponseEntity<?> getPGsByOwner() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        String ownerId = principal.getId();
        List<PG> pgs = pgService.getPGsByOwner(ownerId);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "pgs", pgs
        ));
    }

    /**
     * POST /api/pgs
     * consumes multipart/form-data, like your upload.array("images", 10)
     *
     * Frontend sends:
     *  - title, propertyType, location, address, monthlyRent, deposit,
     *    occupancyType, amenities (JSON string), description
     *  - images[]: files
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPG(
            @RequestParam String title,
            @RequestParam String propertyType,
            @RequestParam String location,
            @RequestParam String address,
            @RequestParam Double monthlyRent,
            @RequestParam Double deposit,
            @RequestParam String occupancyType,
            @RequestParam(required = false) String amenities, // JSON array string
            @RequestParam String description,
            @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) {
        // ✅ Get current user from JWT (like req.user.id)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        String userId = principal.getId();
        String role = principal.getRole();
        if (!"pgowner".equalsIgnoreCase(role)) {
            return ResponseEntity.status(403).body(ApiResponse.failure("Unauthorized: Only PG Owners can create PGs"));
        }

        try {
            PGRequest req = new PGRequest();
            req.setTitle(title);
            req.setPropertyType(propertyType);
            req.setLocation(location);
            req.setAddress(address);
            req.setMonthlyRent(monthlyRent);
            req.setDeposit(deposit);
            req.setOccupancyType(occupancyType);
            req.setDescription(description);
            req.setOwnerId(userId);

            // amenities is JSON string (like '["WiFi","AC"]')
            if (amenities != null && !amenities.isBlank()) {
                List<String> amenityList = objectMapper.readValue(
                        amenities,
                        new TypeReference<List<String>>() {}
                );
                req.setAmenities(amenityList);
            }

            // Handle file uploads
            if (images != null && !images.isEmpty()) {
                List<String> paths = images.stream().map(file -> {
                    try {
                        return fileStorageService.saveFile("pgs", file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
                req.setImagePaths(paths);
            }

            PG created = pgService.createPG(req);

            // Node version:
            // res.json({ success: true, message: "PG listing created successfully", pg })
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "PG listing created successfully",
                    "pg", created
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("Server Error"));
        }
    }
}

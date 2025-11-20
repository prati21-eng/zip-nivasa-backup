package com.zipnivasa.backend.modules.messowner.controller;

import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.modules.messowner.dto.MessOwnerRequest;
import com.zipnivasa.backend.modules.messowner.service.MessOwnerService;
import com.zipnivasa.backend.modules.user.model.MessOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mirrors backend/routes/messOwnerRoutes.js:
 *
 * POST /api/mess-owner/add
 * GET  /api/mess-owner/all
 */
@RestController
@RequestMapping("/api/mess-owner")
@RequiredArgsConstructor
public class MessOwnerController {

    private final MessOwnerService messOwnerService;

    // POST /api/mess-owner/add
    @PostMapping("/add")
    public ResponseEntity<?> addMessOwner(@RequestBody MessOwnerRequest req) {
        try {
            MessOwner owner = messOwnerService.addMessOwner(req);
            return ResponseEntity.ok(
                    ApiResponse.success("Mess owner added successfully", owner)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("Error adding mess owner"));
        }
    }

    // GET /api/mess-owner/all
    @GetMapping("/all")
    public ResponseEntity<List<MessOwner>> getAllMessOwners() {
        List<MessOwner> owners = messOwnerService.getAll();
        return ResponseEntity.ok(owners);
    }
}

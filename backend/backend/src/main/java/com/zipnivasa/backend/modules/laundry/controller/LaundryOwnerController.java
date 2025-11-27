package com.zipnivasa.backend.modules.laundry.controller;

import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.modules.laundry.dto.LaundryOwnerRequest;
import com.zipnivasa.backend.modules.laundry.service.LaundryOwnerService;
import com.zipnivasa.backend.modules.user.model.LaundryOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laundry")
@RequiredArgsConstructor
public class LaundryOwnerController {

    private final LaundryOwnerService laundryOwnerService;

    @PostMapping("/add")
    public ResponseEntity<?> addLaundryOwner(@RequestBody LaundryOwnerRequest req) {
        try {
            LaundryOwner owner = laundryOwnerService.addLaundryOwner(req);
            return ResponseEntity.ok(
                    ApiResponse.success("Laundry owner added successfully", owner)
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.failure("Error adding laundry owner"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<LaundryOwner>> getAll() {
        return ResponseEntity.ok(laundryOwnerService.getAll());
    }
}

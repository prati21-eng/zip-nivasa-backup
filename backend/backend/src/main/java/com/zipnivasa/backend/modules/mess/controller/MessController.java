package com.zipnivasa.backend.modules.mess.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zipnivasa.backend.common.file.FileStorageService;
import com.zipnivasa.backend.common.payload.ApiResponse;
import com.zipnivasa.backend.config.JwtAuthenticationFilter;
import com.zipnivasa.backend.modules.mess.dto.MessRequest;
import com.zipnivasa.backend.modules.mess.model.Mess;
import com.zipnivasa.backend.modules.mess.service.MessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/mess")
@RequiredArgsConstructor
public class MessController {

    private final MessService messService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper mapper = new ObjectMapper();

    // ---------------------------- ADD MESS ---------------------------------

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMess(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam Double price,
            @RequestParam String type,
            @RequestParam Integer capacity,
            @RequestParam(required = false) String menu, // JSON array
            @RequestParam(required = false) String contact,
            @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException {

        // get logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null ||
                !(auth.getPrincipal() instanceof JwtAuthenticationFilter.JwtUserPrincipal principal)) {
            return ResponseEntity.status(401).body(ApiResponse.failure("Unauthorized"));
        }

        if (!"messowner".equals(principal.getRole())) {
            return ResponseEntity.status(403).body(ApiResponse.failure("Only Mess Owners allowed"));
        }

        MessRequest req = new MessRequest();
        req.setMessOwnerId(principal.getId());
        req.setTitle(title);
        req.setDescription(description);
        req.setLocation(location);
        req.setPrice(price);
        req.setType(type);
        req.setCapacity(capacity);

        if (menu != null) {
            List<String> menuList = mapper.readValue(menu, new TypeReference<List<String>>() {});
            req.setMenu(menuList);
        }

        // file upload
        if (images != null) {
            List<String> saved = images.stream()
                    .map(img -> {
                        try {
                            return fileStorageService.saveFile("messes", img);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();

            req.setImages(saved);
        }

        Mess saved = messService.addMess(req);

        return ResponseEntity.ok(ApiResponse.success("Mess added successfully!", saved));
    }

    // ---------------------------- PUBLIC GET ALL ----------------------------

    @GetMapping("/all")
    public List<Mess> getAll() {
        return messService.getAll();
    }

    // ---------------------------- GET BY ID ---------------------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getMessById(@PathVariable String id) {
        Mess mess = messService.getById(id);
        if (mess == null) {
            return ResponseEntity.status(404).body(ApiResponse.failure("Mess not found"));
        }
        return ResponseEntity.ok(mess);
    }

    // ---------------------------- GET BY OWNER ------------------------------

    @GetMapping("/owner/{ownerId}")
    public List<Mess> getByOwner(@PathVariable String ownerId) {
        return messService.getByOwner(ownerId);
    }

    // ---------------------------- UPDATE MESS -------------------------------

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMess(@PathVariable String id, @RequestBody MessRequest req) {
        Mess updated = messService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    // ---------------------------- DELETE MESS -------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMess(@PathVariable String id) {
        messService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Mess deleted", null));
    }

    // ---------------------------- ADD RATING -------------------------------

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> addRating(
            @PathVariable String id,
            @RequestBody Mess.Rating rating
    ) {

        rating.setDate(Instant.now());
        Mess updated = messService.addRating(id, rating);
        return ResponseEntity.ok(ApiResponse.success("Rating added", updated));
    }

    // ---------------------------- SPECIAL TODAY -----------------------------

    @PostMapping("/publish-special")
    public ResponseEntity<?> publishSpecial(@RequestBody Mess.SpecialToday special) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationFilter.JwtUserPrincipal principal =
                (JwtAuthenticationFilter.JwtUserPrincipal) auth.getPrincipal();

        Mess updated = messService.publishSpecial(principal.getId(), special);

        return ResponseEntity.ok(ApiResponse.success("Special updated", updated));
    }
}

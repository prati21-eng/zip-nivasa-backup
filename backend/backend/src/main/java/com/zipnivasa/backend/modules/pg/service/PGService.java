package com.zipnivasa.backend.modules.pg.service;

import com.zipnivasa.backend.modules.pg.dto.PGRequest;
import com.zipnivasa.backend.modules.pg.model.PG;
import com.zipnivasa.backend.modules.pg.repository.PGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PGService {

    private final PGRepository pgRepository;

    public PG createPG(PGRequest req) {
        PG pg = PG.builder()
                .title(req.getTitle())
                .propertyType(req.getPropertyType())
                .location(req.getLocation())
                .address(req.getAddress())
                .monthlyRent(req.getMonthlyRent())
                .deposit(req.getDeposit())
                .occupancyType(req.getOccupancyType())
                .description(req.getDescription())
                .owner(req.getOwnerId())
                .amenities(req.getAmenities())
                .images(req.getImagePaths())
                .beds(1)
                .available(true)
                .views(0)
                .inquiries(0)
                .build();

        return pgRepository.save(pg);
    }

    public List<PG> getAllPGs() {
        return pgRepository.findAllByOrderByCreatedAtDesc();
    }

    public PG getPGById(String id) {
        return pgRepository.findById(id)
                .orElse(null);
    }

    public List<PG> getPGsByOwner(String ownerId) {
        return pgRepository.findByOwnerOrderByCreatedAtDesc(ownerId);
    }
}

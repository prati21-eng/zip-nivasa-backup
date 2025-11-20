package com.zipnivasa.backend.modules.laundry.service;

import com.zipnivasa.backend.modules.laundry.dto.LaundryOwnerRequest;
import com.zipnivasa.backend.modules.user.model.LaundryOwner;
import com.zipnivasa.backend.modules.user.repository.LaundryOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles LaundryOwner CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class LaundryOwnerService {

    private final LaundryOwnerRepository laundryOwnerRepository;

    public LaundryOwner addLaundryOwner(LaundryOwnerRequest req) {

        LaundryOwner owner = LaundryOwner.builder()
                .userId(req.getUserId())
                .laundryName(req.getLaundryName())
                .serviceLocation(req.getServiceLocation())
                .ratePerKg(req.getRatePerKg())
                .build();

        return laundryOwnerRepository.save(owner);
    }

    public List<LaundryOwner> getAll() {
        return laundryOwnerRepository.findAll();
    }
}

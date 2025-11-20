package com.zipnivasa.backend.modules.messowner.service;

import com.zipnivasa.backend.modules.messowner.dto.MessOwnerRequest;
import com.zipnivasa.backend.modules.user.model.MessOwner;
import com.zipnivasa.backend.modules.user.repository.MessOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessOwnerService {

    private final MessOwnerRepository messOwnerRepository;

    public MessOwner addMessOwner(MessOwnerRequest req) {
        MessOwner owner = MessOwner.builder()
                .userId(req.getUserId())
                .messName(req.getMessName())
                .messLocation(req.getMessLocation())
                .messCapacity(req.getMessCapacity())
                .messType(req.getMessType())
                .build();

        return messOwnerRepository.save(owner);
    }

    public List<MessOwner> getAll() {
        return messOwnerRepository.findAll();
    }
}

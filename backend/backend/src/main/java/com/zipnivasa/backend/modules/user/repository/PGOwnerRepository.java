package com.zipnivasa.backend.modules.user.repository;

import com.zipnivasa.backend.modules.user.model.PGOwner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PGOwnerRepository extends MongoRepository<PGOwner, String> {

    Optional<PGOwner> findByUserId(String userId);
}

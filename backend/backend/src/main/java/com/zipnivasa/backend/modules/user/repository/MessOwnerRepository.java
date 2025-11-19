package com.zipnivasa.backend.modules.user.repository;

import com.zipnivasa.backend.modules.user.model.MessOwner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MessOwnerRepository extends MongoRepository<MessOwner, String> {

    Optional<MessOwner> findByUserId(String userId);
}

package com.zipnivasa.backend.modules.user.repository;

import com.zipnivasa.backend.modules.user.model.LaundryOwner;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LaundryOwnerRepository extends MongoRepository<LaundryOwner, String> {

    Optional<LaundryOwner> findByUserId(String userId);
}

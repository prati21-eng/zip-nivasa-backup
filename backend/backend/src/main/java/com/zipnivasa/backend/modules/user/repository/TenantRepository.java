package com.zipnivasa.backend.modules.user.repository;

import com.zipnivasa.backend.modules.user.model.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TenantRepository extends MongoRepository<Tenant, String> {

    Optional<Tenant> findByUserId(String userId);
}

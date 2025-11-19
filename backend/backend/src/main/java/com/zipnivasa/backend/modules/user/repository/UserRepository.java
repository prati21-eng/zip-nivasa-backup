package com.zipnivasa.backend.modules.user.repository;

import com.zipnivasa.backend.modules.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Replaces: User.findOne({ email }), User.findById(...)
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

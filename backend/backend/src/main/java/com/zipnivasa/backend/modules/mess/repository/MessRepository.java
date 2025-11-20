package com.zipnivasa.backend.modules.mess.repository;

import com.zipnivasa.backend.modules.mess.model.Mess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessRepository extends MongoRepository<Mess, String> {

    List<Mess> findByMessOwnerId(String ownerId);
}

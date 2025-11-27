package com.zipnivasa.backend.modules.pg.repository;

import com.zipnivasa.backend.modules.pg.model.PG;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PGRepository extends MongoRepository<PG, String> {

    List<PG> findByOwnerOrderByCreatedAtDesc(String owner);

    List<PG> findAllByOrderByCreatedAtDesc();
}

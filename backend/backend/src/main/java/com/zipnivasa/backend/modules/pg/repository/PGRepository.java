package com.zipnivasa.backend.modules.pg.repository;

import com.zipnivasa.backend.modules.pg.model.PG;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
  Equivalent of PG.find(), PG.findById(), PG.find({ owner: ... })
 */
public interface PGRepository extends MongoRepository<PG, String> {

    List<PG> findByOwnerOrderByCreatedAtDesc(String owner);

    List<PG> findAllByOrderByCreatedAtDesc();
}

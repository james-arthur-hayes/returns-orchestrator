package com.jhayes.returns.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ManifestRepository extends ReactiveCrudRepository<ReturnManifest, UUID> {
}
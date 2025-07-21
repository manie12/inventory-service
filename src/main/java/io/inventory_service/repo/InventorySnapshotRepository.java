package io.inventory_service.repo;

import io.inventory_service.models.InventorySnapshot;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InventorySnapshotRepository
        extends ReactiveCrudRepository<InventorySnapshot, UUID> {
    /* 100 % CRUD supplied by Spring Data R2DBC */
}
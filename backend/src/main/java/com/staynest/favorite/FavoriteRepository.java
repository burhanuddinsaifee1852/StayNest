package com.staynest.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    List<Favorite> findAllByUser_IdOrderByCreatedAtDesc(UUID userId);

    Optional<Favorite> findByUser_IdAndPropertyId(UUID userId, UUID propertyId);
}

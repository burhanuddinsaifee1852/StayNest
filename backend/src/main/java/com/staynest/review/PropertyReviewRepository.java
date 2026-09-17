package com.staynest.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyReviewRepository extends JpaRepository<PropertyReview, UUID> {

    Page<PropertyReview> findByPropertyIdAndVisibleTrueOrderByCreatedAtDesc(UUID propertyId, Pageable pageable);

    List<PropertyReview> findAllByUser_IdOrderByUpdatedAtDesc(UUID userId);

    Optional<PropertyReview> findByIdAndUser_Id(UUID reviewId, UUID userId);

    boolean existsByPropertyIdAndUser_Id(UUID propertyId, UUID userId);

    @Query("select avg(r.rating) from PropertyReview r where r.propertyId = :propertyId and r.visible = true")
    Double findAverageRatingByPropertyId(UUID propertyId);

    long countByPropertyIdAndVisibleTrue(UUID propertyId);
}

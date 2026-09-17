package com.staynest.review;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PropertyReviewResponse(
        UUID id,
        UUID propertyId,
        int rating,
        String comment,
        String reviewerName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    static PropertyReviewResponse from(PropertyReview review) {
        String lastName = review.getUser().getLastName();
        String reviewerName = lastName == null || lastName.isBlank()
                ? review.getUser().getFirstName()
                : review.getUser().getFirstName() + " " + lastName;
        return new PropertyReviewResponse(
                review.getId(), review.getPropertyId(), review.getRating(), review.getComment(), reviewerName,
                review.getCreatedAt(), review.getUpdatedAt());
    }
}

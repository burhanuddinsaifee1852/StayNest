package com.staynest.review;

import java.util.List;

public record PropertyReviewPageResponse(
        List<PropertyReviewResponse> reviews,
        long totalReviews,
        Double averageRating,
        int page,
        int size,
        int totalPages
) {
}

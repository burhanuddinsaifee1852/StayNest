package com.staynest.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateReviewRequest(
        @Min(value = 1, message = "Rating must be between 1 and 5.")
        @Max(value = 5, message = "Rating must be between 1 and 5.")
        int rating,

        @NotBlank(message = "Review text is required.")
        @Size(max = 2000, message = "Review text must be at most 2000 characters.")
        String comment
) {
}

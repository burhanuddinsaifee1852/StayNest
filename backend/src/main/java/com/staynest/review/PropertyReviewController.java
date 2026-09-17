package com.staynest.review;

import com.staynest.common.security.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api")
public class PropertyReviewController {

    private final CurrentUser currentUser;
    private final PropertyReviewService reviewService;

    public PropertyReviewController(CurrentUser currentUser, PropertyReviewService reviewService) {
        this.currentUser = currentUser;
        this.reviewService = reviewService;
    }

    @GetMapping("/properties/{propertyId}/reviews")
    public PropertyReviewPageResponse getPropertyReviews(
            @PathVariable UUID propertyId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size) {
        return reviewService.getPropertyReviews(propertyId, page, size);
    }

    @GetMapping("/me/reviews")
    public List<PropertyReviewResponse> getMyReviews(Authentication authentication) {
        return reviewService.getMyReviews(currentUser.id(authentication));
    }

    @PostMapping("/properties/{propertyId}/reviews")
    public ResponseEntity<PropertyReviewResponse> createReview(
            @PathVariable UUID propertyId,
            Authentication authentication,
            @Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(currentUser.id(authentication), propertyId, request));
    }

    @PutMapping("/reviews/{reviewId}")
    public PropertyReviewResponse updateReview(
            @PathVariable UUID reviewId,
            Authentication authentication,
            @Valid @RequestBody UpdateReviewRequest request) {
        return reviewService.update(currentUser.id(authentication), reviewId, request);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId, Authentication authentication) {
        reviewService.delete(currentUser.id(authentication), reviewId);
        return ResponseEntity.noContent().build();
    }
}

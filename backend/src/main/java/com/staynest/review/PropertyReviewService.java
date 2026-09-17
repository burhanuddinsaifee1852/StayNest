package com.staynest.review;

import com.staynest.user.User;
import com.staynest.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PropertyReviewService {

    private static final int MAX_PAGE_SIZE = 50;

    private final PropertyReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public PropertyReviewService(PropertyReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public PropertyReviewPageResponse getPropertyReviews(UUID propertyId, int page, int size) {
        Page<PropertyReview> reviews = reviewRepository.findByPropertyIdAndVisibleTrueOrderByCreatedAtDesc(
                propertyId, PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE)));
        Double averageRating = reviewRepository.findAverageRatingByPropertyId(propertyId);

        return new PropertyReviewPageResponse(
                reviews.getContent().stream().map(PropertyReviewResponse::from).toList(),
                reviews.getTotalElements(),
                averageRating,
                reviews.getNumber(),
                reviews.getSize(),
                reviews.getTotalPages());
    }

    public List<PropertyReviewResponse> getMyReviews(UUID userId) {
        return reviewRepository.findAllByUser_IdOrderByUpdatedAtDesc(userId).stream()
                .map(PropertyReviewResponse::from)
                .toList();
    }

    @Transactional
    public PropertyReviewResponse create(UUID userId, UUID propertyId, CreateReviewRequest request) {
        if (reviewRepository.existsByPropertyIdAndUser_Id(propertyId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "You have already reviewed this property. Update your existing review instead.");
        }
        User user = findUser(userId);
        PropertyReview review = reviewRepository.save(new PropertyReview(
                user, propertyId, request.rating(), request.comment().trim()));
        return PropertyReviewResponse.from(review);
    }

    @Transactional
    public PropertyReviewResponse update(UUID userId, UUID reviewId, UpdateReviewRequest request) {
        PropertyReview review = reviewRepository.findByIdAndUser_Id(reviewId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review was not found."));
        review.setRating(request.rating());
        review.setComment(request.comment().trim());
        return PropertyReviewResponse.from(review);
    }

    @Transactional
    public void delete(UUID userId, UUID reviewId) {
        PropertyReview review = reviewRepository.findByIdAndUser_Id(reviewId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review was not found."));
        reviewRepository.delete(review);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found."));
    }
}

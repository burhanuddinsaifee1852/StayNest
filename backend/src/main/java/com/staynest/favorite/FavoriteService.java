package com.staynest.favorite;

import com.staynest.user.User;
import com.staynest.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public List<FavoriteResponse> getFavorites(UUID userId) {
        return favoriteRepository.findAllByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(FavoriteResponse::from)
                .toList();
    }

    /** Saving the same property twice is deliberately idempotent for retry-safe UI actions. */
    @Transactional
    public FavoriteResponse save(UUID userId, UUID propertyId) {
        return favoriteRepository.findByUser_IdAndPropertyId(userId, propertyId)
                .map(FavoriteResponse::from)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found."));
                    Favorite favorite = favoriteRepository.save(new Favorite(user, propertyId));
                    return FavoriteResponse.from(favorite);
                });
    }

    /** Removing an already-removed property is also idempotent. */
    @Transactional
    public void remove(UUID userId, UUID propertyId) {
        favoriteRepository.findByUser_IdAndPropertyId(userId, propertyId)
                .ifPresent(favoriteRepository::delete);
    }
}

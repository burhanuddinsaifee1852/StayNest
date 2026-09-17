package com.staynest.favorite;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FavoriteResponse(UUID propertyId, OffsetDateTime savedAt) {
    static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getPropertyId(), favorite.getCreatedAt());
    }
}

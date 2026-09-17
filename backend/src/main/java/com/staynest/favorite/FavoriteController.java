package com.staynest.favorite;

import com.staynest.common.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FavoriteController {

    private final CurrentUser currentUser;
    private final FavoriteService favoriteService;

    public FavoriteController(CurrentUser currentUser, FavoriteService favoriteService) {
        this.currentUser = currentUser;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/me/favorites")
    public List<FavoriteResponse> getFavorites(Authentication authentication) {
        return favoriteService.getFavorites(currentUser.id(authentication));
    }

    @PutMapping("/properties/{propertyId}/favorite")
    public ResponseEntity<FavoriteResponse> save(@PathVariable UUID propertyId, Authentication authentication) {
        return ResponseEntity.ok(favoriteService.save(currentUser.id(authentication), propertyId));
    }

    @DeleteMapping("/properties/{propertyId}/favorite")
    public ResponseEntity<Void> remove(@PathVariable UUID propertyId, Authentication authentication) {
        favoriteService.remove(currentUser.id(authentication), propertyId);
        return ResponseEntity.noContent().build();
    }
}

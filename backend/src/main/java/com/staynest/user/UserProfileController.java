package com.staynest.user;

import com.staynest.common.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me/profile")
public class UserProfileController {

    private final CurrentUser currentUser;
    private final UserProfileService userProfileService;

    public UserProfileController(CurrentUser currentUser, UserProfileService userProfileService) {
        this.currentUser = currentUser;
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public UserProfileResponse getProfile(Authentication authentication) {
        return userProfileService.getProfile(currentUser.id(authentication));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(currentUser.id(authentication), request));
    }
}

package com.staynest.common.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * Boundary between feature modules and the authentication module.  The JWT
 * implementation should expose the authenticated user's UUID as its principal
 * (or authentication name); feature controllers never trust a client supplied
 * user id.
 */
@Component
public class CurrentUser {

    public UUID id(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication is required.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID userId) {
            return userId;
        }

        String candidate = principal instanceof String value ? value : authentication.getName();
        try {
            return UUID.fromString(candidate);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Authenticated user identity is invalid.");
        }
    }
}

package com.staynest.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank(message = "First name is required.")
        @Size(max = 100, message = "First name must be at most 100 characters.")
        String firstName,

        @Size(max = 100, message = "Last name must be at most 100 characters.")
        String lastName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Enter a valid email address.")
        @Size(max = 255, message = "Email must be at most 255 characters.")
        String email,

        @Pattern(regexp = "^[0-9+() -]{7,20}$", message = "Enter a valid phone number.")
        String phone
) {
}

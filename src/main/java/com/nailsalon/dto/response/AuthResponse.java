package com.nailsalon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;
    private String refreshToken;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}
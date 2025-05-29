package com.nailsalon.service;

import com.nailsalon.dto.request.LoginRequest;
import com.nailsalon.dto.request.RegisterRequest;
import com.nailsalon.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse refreshToken(String refreshToken);
}
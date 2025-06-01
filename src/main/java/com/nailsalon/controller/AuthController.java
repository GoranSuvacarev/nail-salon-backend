package com.nailsalon.controller;

import com.nailsalon.dto.request.LoginRequest;
import com.nailsalon.dto.request.RegisterRequest;
import com.nailsalon.dto.response.AuthResponse;
import com.nailsalon.dto.response.TokenRefreshResponse;
import com.nailsalon.entity.User;
import com.nailsalon.repository.UserRepository;
import com.nailsalon.security.CustomUserDetails;
import com.nailsalon.security.JwtTokenProvider;
import com.nailsalon.service.AuthService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/register-staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<AuthResponse> registerStaff(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setRole(User.Role.STAFF);

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(savedUser.getEmail());

        return ResponseEntity.ok(new AuthResponse(
                savedUser.getId(),
                token,
                refreshToken,
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new TokenRefreshResponse(authResponse.getToken()));
    }

    @GetMapping("/token-info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getTokenInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<String, Object> info = new HashMap<>();
        info.put("email", userDetails.getUsername());
        info.put("role", userDetails.getUser().getRole().name());
        info.put("expiresIn", tokenProvider.getExpirationInSeconds());
        return ResponseEntity.ok(info);
    }

    @Data
    static class RefreshTokenRequest {
        private String refreshToken;
    }
}
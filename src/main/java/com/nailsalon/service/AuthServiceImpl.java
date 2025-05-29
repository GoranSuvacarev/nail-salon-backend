package com.nailsalon.service;

import com.nailsalon.dto.request.LoginRequest;
import com.nailsalon.dto.request.RegisterRequest;
import com.nailsalon.dto.response.AuthResponse;
import com.nailsalon.entity.User;
import com.nailsalon.repository.UserRepository;
import com.nailsalon.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        return new AuthResponse(
                user.getId(),
                token,
                refreshToken,
                user.getEmail(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setRole(User.Role.CUSTOMER);  // Default role

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(savedUser.getEmail(), savedUser.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(savedUser.getEmail());

        return new AuthResponse(
                savedUser.getId(),
                token,
                refreshToken,
                savedUser.getEmail(),
                savedUser.getRole().name(),
                savedUser.getFirstName(),
                savedUser.getLastName()
        );
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = tokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = tokenProvider.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(
                user.getId(),
                newToken,
                refreshToken,
                user.getEmail(),
                user.getRole().name(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
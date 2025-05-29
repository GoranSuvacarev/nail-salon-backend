package com.nailsalon.controller;

import com.nailsalon.entity.Appointment;
import com.nailsalon.entity.User;
import com.nailsalon.security.CustomUserDetails;
import com.nailsalon.service.AppointmentService;
import com.nailsalon.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    private final UserService userService;
    private final AppointmentService appointmentService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUser());
    }

    @PutMapping
    public ResponseEntity<User> updateMyProfile(@RequestBody UpdateProfileRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        User updatedUser = userService.updateUser(user.getId(), user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.updateUser(user.getId(), user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMyAppointments(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        if (user.getRole() == User.Role.CUSTOMER) {
            return ResponseEntity.ok(appointmentService.getCustomerAppointments(user.getId()));
        } else {
            return ResponseEntity.ok(appointmentService.getStaffAppointments(user.getId()));
        }
    }

    @Data
    static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String phone;
    }

    @Data
    static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}
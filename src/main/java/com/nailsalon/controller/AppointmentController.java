package com.nailsalon.controller;

import com.nailsalon.dto.request.AppointmentRequest;
import com.nailsalon.entity.Appointment;
import com.nailsalon.entity.User;
import com.nailsalon.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.nailsalon.security.CustomUserDetails;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody AppointmentRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Ensure customer can only book for themselves
        if (!userDetails.getUser().getRole().equals(User.Role.CUSTOMER) ||
                !request.getCustomerId().equals(userDetails.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Appointment appointment = appointmentService.createAppointment(
                request.getCustomerId(),
                request.getStaffId(),
                request.getServiceId(),
                request.getAppointmentDate(),
                request.getStartTime()
        );
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // Customers can see their own appointments, staff can see any
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('STAFF') or #customerId == authentication.principal.user.id")
    public ResponseEntity<List<Appointment>> getCustomerAppointments(@PathVariable Long customerId) {
        return ResponseEntity.ok(appointmentService.getCustomerAppointments(customerId));
    }

    // Only staff can see staff schedules
    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<Appointment>> getStaffAppointments(@PathVariable Long staffId) {
        return ResponseEntity.ok(appointmentService.getStaffAppointments(staffId));
    }

    @GetMapping("/staff/{staffId}/date/{date}")
    public ResponseEntity<List<Appointment>> getStaffAppointmentsByDate(
            @PathVariable Long staffId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getStaffAppointmentsByDate(staffId, date));
    }

    // Only staff can mark appointments as complete
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Appointment> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, Appointment.AppointmentStatus.COMPLETED));
    }

    // Customers can cancel their own appointments
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('STAFF')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Appointment appointment = appointmentService.getAppointmentById(id);

        // Check if customer is canceling their own appointment
        if (userDetails.getUser().getRole().equals(User.Role.CUSTOMER) &&
                !appointment.getCustomer().getId().equals(userDetails.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
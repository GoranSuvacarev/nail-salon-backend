package com.nailsalon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time is required")
    private String startTime; // Format: "HH:mm"
}
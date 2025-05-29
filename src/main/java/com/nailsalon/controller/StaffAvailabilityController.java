package com.nailsalon.controller;

import com.nailsalon.entity.StaffAvailability;
import com.nailsalon.service.StaffAvailabilityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/staff-availability")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffAvailabilityController {

    private final StaffAvailabilityService availabilityService;

    @PostMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<StaffAvailability> setAvailability(@RequestBody AvailabilityRequest request) {
        StaffAvailability availability = availabilityService.setStaffAvailability(
                request.getStaffId(),
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime()
        );
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<StaffAvailability>> getStaffAvailability(@PathVariable Long staffId) {
        return ResponseEntity.ok(availabilityService.getStaffAvailability(staffId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> removeAvailability(@PathVariable Long id) {
        availabilityService.removeAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<Void> toggleAvailability(@PathVariable Long id) {
        availabilityService.toggleAvailability(id);
        return ResponseEntity.ok().build();
    }

    @Data
    static class AvailabilityRequest {
        private Long staffId;
        private DayOfWeek dayOfWeek;
        private String startTime;
        private String endTime;
    }
}
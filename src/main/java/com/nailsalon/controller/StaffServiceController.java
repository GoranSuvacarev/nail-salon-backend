package com.nailsalon.controller;

import com.nailsalon.entity.Service;
import com.nailsalon.entity.User;
import com.nailsalon.service.StaffServiceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/staff-services")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StaffServiceController {

    private final StaffServiceManagementService staffServiceManagementService;

    @PostMapping("/staff/{staffId}/service/{serviceId}")
    public ResponseEntity<Void> addServiceToStaff(@PathVariable Long staffId,
                                                  @PathVariable Long serviceId) {
        staffServiceManagementService.addServiceToStaff(staffId, serviceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/staff/{staffId}/service/{serviceId}")
    public ResponseEntity<Void> removeServiceFromStaff(@PathVariable Long staffId,
                                                       @PathVariable Long serviceId) {
        staffServiceManagementService.removeServiceFromStaff(staffId, serviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<Service>> getStaffServices(@PathVariable Long staffId) {
        return ResponseEntity.ok(staffServiceManagementService.getStaffServices(staffId));
    }

    @GetMapping("/service/{serviceId}/specialists")
    public ResponseEntity<List<User>> getServiceSpecialists(@PathVariable Long serviceId) {
        return ResponseEntity.ok(staffServiceManagementService.getServiceSpecialists(serviceId));
    }
}
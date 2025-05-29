package com.nailsalon.service;

import com.nailsalon.entity.Service;
import com.nailsalon.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceManagementServiceImpl implements StaffServiceManagementService {

    private final UserService userService;
    private final ServiceService serviceService;

    @Override
    public void addServiceToStaff(Long staffId, Long serviceId) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getRole() != User.Role.STAFF) {
            throw new RuntimeException("User is not a staff member");
        }

        Service service = serviceService.getServiceById(serviceId);

        if (!staff.getSpecializedServices().contains(service)) {
            staff.getSpecializedServices().add(service);
            userService.updateUser(staffId, staff);
        }
    }

    @Override
    public void removeServiceFromStaff(Long staffId, Long serviceId) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Service service = serviceService.getServiceById(serviceId);

        staff.getSpecializedServices().remove(service);
        userService.updateUser(staffId, staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getStaffServices(Long staffId) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return staff.getSpecializedServices();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getServiceSpecialists(Long serviceId) {
        Service service = serviceService.getServiceById(serviceId);
        return service.getSpecialists();
    }
}
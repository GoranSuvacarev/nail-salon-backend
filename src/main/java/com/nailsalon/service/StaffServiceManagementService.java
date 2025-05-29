package com.nailsalon.service;

import com.nailsalon.entity.Service;
import com.nailsalon.entity.User;
import java.util.List;

public interface StaffServiceManagementService {

    void addServiceToStaff(Long staffId, Long serviceId);

    void removeServiceFromStaff(Long staffId, Long serviceId);

    List<Service> getStaffServices(Long staffId);

    List<User> getServiceSpecialists(Long serviceId);
}
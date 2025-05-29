package com.nailsalon.service;

import com.nailsalon.entity.Service;
import java.util.List;

public interface ServiceService {

    Service createService(Service service);

    Service updateService(Long id, Service service);

    void deleteService(Long id);

    Service getServiceById(Long id);

    List<Service> getAllServices();

    List<Service> getServicesByCategory(Service.ServiceCategory category);
}
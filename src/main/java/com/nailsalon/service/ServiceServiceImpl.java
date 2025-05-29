package com.nailsalon.service;

import com.nailsalon.entity.Service;
import com.nailsalon.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public Service updateService(Long id, Service service) {
        Service existingService = getServiceById(id);
        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setPrice(service.getPrice());
        existingService.setDurationMinutes(service.getDurationMinutes());
        existingService.setCategory(service.getCategory());
        return serviceRepository.save(existingService);
    }

    @Override
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> getServicesByCategory(Service.ServiceCategory category) {
        return serviceRepository.findByCategory(category);
    }
}
package com.nailsalon.service;

import com.nailsalon.entity.StaffAvailability;
import com.nailsalon.entity.User;
import com.nailsalon.repository.StaffAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffAvailabilityServiceImpl implements StaffAvailabilityService {

    private final StaffAvailabilityRepository availabilityRepository;
    private final UserService userService;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public StaffAvailability setStaffAvailability(Long staffId, DayOfWeek dayOfWeek,
                                                  String startTime, String endTime) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (staff.getRole() != User.Role.STAFF) {
            throw new RuntimeException("User is not a staff member");
        }

        // Check if availability already exists for this day
        StaffAvailability availability = availabilityRepository
                .findByStaffAndDayOfWeek(staff, dayOfWeek)
                .orElse(new StaffAvailability());

        availability.setStaff(staff);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(LocalTime.parse(startTime, timeFormatter));
        availability.setEndTime(LocalTime.parse(endTime, timeFormatter));
        availability.setIsAvailable(true);

        return availabilityRepository.save(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffAvailability> getStaffAvailability(Long staffId) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return availabilityRepository.findByStaff(staff);
    }

    @Override
    public void removeAvailability(Long availabilityId) {
        availabilityRepository.deleteById(availabilityId);
    }

    @Override
    public void toggleAvailability(Long availabilityId) {
        StaffAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        availability.setIsAvailable(!availability.getIsAvailable());
        availabilityRepository.save(availability);
    }
}
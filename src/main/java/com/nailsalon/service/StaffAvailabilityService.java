package com.nailsalon.service;

import com.nailsalon.entity.StaffAvailability;
import java.time.DayOfWeek;
import java.util.List;

public interface StaffAvailabilityService {

    StaffAvailability setStaffAvailability(Long staffId, DayOfWeek dayOfWeek,
                                           String startTime, String endTime);

    List<StaffAvailability> getStaffAvailability(Long staffId);

    void removeAvailability(Long availabilityId);

    void toggleAvailability(Long availabilityId);
}
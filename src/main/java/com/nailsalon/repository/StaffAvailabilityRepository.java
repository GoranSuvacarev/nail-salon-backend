package com.nailsalon.repository;

import com.nailsalon.entity.StaffAvailability;
import com.nailsalon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffAvailabilityRepository extends JpaRepository<StaffAvailability, Long> {

    List<StaffAvailability> findByStaff(User staff);

    List<StaffAvailability> findByStaffAndIsAvailableTrue(User staff);

    Optional<StaffAvailability> findByStaffAndDayOfWeek(User staff, DayOfWeek dayOfWeek);
}
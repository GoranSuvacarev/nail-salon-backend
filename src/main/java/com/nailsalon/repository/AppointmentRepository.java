package com.nailsalon.repository;

import com.nailsalon.entity.Appointment;
import com.nailsalon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByCustomer(User customer);

    List<Appointment> findByStaff(User staff);

    List<Appointment> findByStaffAndAppointmentDate(User staff, LocalDate date);

    List<Appointment> findByCustomerAndStatus(User customer, Appointment.AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.staff.id = :staffId " +
            "AND a.appointmentDate = :date " +
            "AND a.status != 'CANCELLED' " +
            "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    List<Appointment> findConflictingAppointments(@Param("staffId") Long staffId,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);
}
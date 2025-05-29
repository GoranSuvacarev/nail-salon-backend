package com.nailsalon.service;

import com.nailsalon.entity.Appointment;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment createAppointment(Long customerId, Long staffId, Long serviceId,
                                  LocalDate date, String startTime);

    Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status);

    void cancelAppointment(Long id);

    Appointment getAppointmentById(Long id);

    List<Appointment> getCustomerAppointments(Long customerId);

    List<Appointment> getStaffAppointments(Long staffId);

    List<Appointment> getStaffAppointmentsByDate(Long staffId, LocalDate date);

    boolean isTimeSlotAvailable(Long staffId, LocalDate date, String startTime, String endTime);
}
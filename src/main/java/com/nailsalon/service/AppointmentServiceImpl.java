package com.nailsalon.service;

import com.nailsalon.entity.Appointment;
import com.nailsalon.entity.Service;
import com.nailsalon.entity.User;
import com.nailsalon.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final ServiceService serviceService;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Appointment createAppointment(Long customerId, Long staffId, Long serviceId,
                                         LocalDate date, String startTime) {

        // Validate customer
        User customer = userService.getUserById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (customer.getRole() != User.Role.CUSTOMER) {
            throw new RuntimeException("User is not a customer");
        }

        // Validate staff
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        if (staff.getRole() != User.Role.STAFF) {
            throw new RuntimeException("User is not a staff member");
        }

        // Get service
        Service service = serviceService.getServiceById(serviceId);

        // Calculate end time
        LocalTime start = LocalTime.parse(startTime, timeFormatter);
        LocalTime end = start.plusMinutes(service.getDurationMinutes());

        // Check availability
        if (!isTimeSlotAvailable(staffId, date, startTime, end.format(timeFormatter))) {
            throw new RuntimeException("Time slot is not available");
        }

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setStaff(staff);
        appointment.setService(service);
        appointment.setAppointmentDate(date);
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void cancelAppointment(Long id) {
        updateAppointmentStatus(id, Appointment.AppointmentStatus.CANCELLED);
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getCustomerAppointments(Long customerId) {
        User customer = userService.getUserById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return appointmentRepository.findByCustomer(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getStaffAppointments(Long staffId) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return appointmentRepository.findByStaff(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getStaffAppointmentsByDate(Long staffId, LocalDate date) {
        User staff = userService.getUserById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return appointmentRepository.findByStaffAndAppointmentDate(staff, date);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTimeSlotAvailable(Long staffId, LocalDate date, String startTime, String endTime) {
        LocalTime start = LocalTime.parse(startTime, timeFormatter);
        LocalTime end = LocalTime.parse(endTime, timeFormatter);

        List<Appointment> conflicts = appointmentRepository
                .findConflictingAppointments(staffId, date, start, end);

        return conflicts.isEmpty();
    }
}
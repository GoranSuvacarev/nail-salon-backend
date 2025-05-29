# Nail Salon Management System - Backend

## Tech Stack
- Spring Boot 3.2.0
- Java 17
- MySQL 8.0 (Docker)
- Spring Security with JWT
- Spring Data JPA

## Features Implemented
- ✅ User Management (CRUD)
- ✅ Service Management
- ✅ Appointment Booking System
- ✅ Staff Availability Management
- ✅ JWT Authentication & Authorization
- ✅ Role-based Access Control (CUSTOMER, STAFF)
- ✅ Token Refresh Mechanism
- ✅ OneToMany and ManyToMany Relationships

## Database Schema
- users (with roles: CUSTOMER, STAFF)
- services (nail services offered)
- appointments
- staff_availability
- user_services (junction table)

## Running the Project
1. Start MySQL Docker: `docker-compose up -d`
2. Run Spring Boot application
3. API available at: http://localhost:8080

## Test Credentials
- Staff: admin@nailsalon.com / admin123
- Customer: customer@example.com / password123

## API Documentation
- Postman collection: [To be added]
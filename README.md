# Nail Salon Management System - Backend

## Tech Stack
- Spring Boot 3.2.0
- Java 17
- MySQL 8.0 (Docker)
- Spring Security with JWT
- Spring Data JPA
- Docker Desktop

## Features Implemented
-  User Management (CRUD)
-  Service Management
-  Appointment Booking System
-  Staff Availability Management
-  JWT Authentication & Authorization
-  Role-based Access Control (CUSTOMER, STAFF)
-  Token Refresh Mechanism
-  OneToMany and ManyToMany Relationships

## Database Schema
- users (with roles: CUSTOMER, STAFF)
- services (nail services offered)
- appointments
- staff_availability
- user_services (junction table)

## Running the Project
1. Start MySQL Docker: `docker-compose up -d`
2. Run Spring Boot application: `./mvnw spring-boot:run`
3. API available at: http://localhost:8080

## Frontend Repository

Frontend: [https://github.com/GoranSuvacarev/nail-salon-backend](https://github.com/GoranSuvacarev/nail-salon-frontend)

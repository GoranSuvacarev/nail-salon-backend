CREATE DATABASE nail_salon;
USE nail_salon;

-- Services table (no dependencies)
CREATE TABLE IF NOT EXISTS services (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    category         ENUM ('GEL', 'MANICURE', 'NAIL_ART', 'PEDICURE', 'TREATMENT') NOT NULL,
    created_at       DATETIME(6)                                                   NULL,
    description      TEXT                                                          NULL,
    duration_minutes INT                                                           NOT NULL,
    name             VARCHAR(255)                                                  NOT NULL,
    price            DECIMAL(38, 2)                                                NOT NULL
);

-- Users table (no dependencies)
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6)                NULL,
    email      VARCHAR(255)               NOT NULL,
    first_name VARCHAR(255)               NOT NULL,
    last_name  VARCHAR(255)               NOT NULL,
    password   VARCHAR(255)               NOT NULL,
    phone      VARCHAR(255)               NOT NULL,
    role       ENUM ('CUSTOMER', 'STAFF') NOT NULL,
    updated_at DATETIME(6)                NULL,
    CONSTRAINT UK6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
);

-- Appointments table (references users and services)
CREATE TABLE IF NOT EXISTS appointments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_date DATE                                         NOT NULL,
    created_at       DATETIME(6)                                  NULL,
    end_time         TIME(6)                                      NOT NULL,
    start_time       TIME(6)                                      NOT NULL,
    status           ENUM ('CANCELLED', 'COMPLETED', 'SCHEDULED') NOT NULL,
    customer_id      BIGINT                                       NOT NULL,
    service_id       BIGINT                                       NOT NULL,
    staff_id         BIGINT                                       NOT NULL,
    CONSTRAINT FK4q5rt20vvnkv7eohwq22l3ayy
        FOREIGN KEY (customer_id) REFERENCES users (id),
    CONSTRAINT FK5iltr7k9pows18hk8nc101vc1
        FOREIGN KEY (service_id) REFERENCES services (id),
    CONSTRAINT FK88083ngr9rv9wj4p916pj40c2
        FOREIGN KEY (staff_id) REFERENCES users (id)
);

-- Staff availability table (references users)
CREATE TABLE IF NOT EXISTS staff_availability (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week  ENUM ('FRIDAY', 'MONDAY', 'SATURDAY', 'SUNDAY', 'THURSDAY', 'TUESDAY', 'WEDNESDAY') NOT NULL,
    end_time     TIME(6)                                                                             NOT NULL,
    is_available BIT                                                                                 NOT NULL,
    start_time   TIME(6)                                                                             NOT NULL,
    staff_id     BIGINT                                                                              NOT NULL,
    CONSTRAINT FK65fh2lb5nwtf3h2hohk59gnbv
        FOREIGN KEY (staff_id) REFERENCES users (id)
);

-- User services junction table (many-to-many relationship)
CREATE TABLE IF NOT EXISTS user_services (
    staff_id   BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    CONSTRAINT FKh9khxfj76ygoeehig761i8t4v
        FOREIGN KEY (staff_id) REFERENCES users (id),
    CONSTRAINT FKntyngsjncr8dgdfy1j9pfn43t
        FOREIGN KEY (service_id) REFERENCES services (id)
);

-- =====================================================
-- DATA INSERTION
-- =====================================================

-- Insert Users (Staff and Customers)
INSERT INTO users (id, created_at, email, first_name, last_name, password, phone, role, updated_at) VALUES 
(14, '2025-05-31 17:22:45.368966', 'lena@nailsalon.com', 'Lena', ' Nguyen', '$2a$10$f2oE337E7Z8JXuadjQ7kwOEsiAYQpWp/rE/R7XDFG9EK0JcoUG/8q', '+381 64 201 3412', 'STAFF', '2025-05-31 17:22:45.368966'),
(15, '2025-05-31 17:23:14.436952', 'maria@nailsalon.com', 'Maria', 'Torres', '$2a$10$1YGfzvaDUM9q49/7pytN9eNY7g/QbosnzORs.NmzKfR0qaBDXzN3m', '+381 64 201 7634', 'STAFF', '2025-05-31 17:23:14.436952'),
(16, '2025-05-31 17:23:35.875388', 'chloe@nailsalon.com', 'Chloe', 'Kim', '$2a$10$oNHLm.tvtkf3X7Uoitf3xezIKwjepXfl8uS8EeBCEKC9i36VSe5uO', '+381 64 201 1298', 'STAFF', '2025-05-31 17:23:35.875388'),
(17, '2025-05-31 17:23:59.397648', 'tanya@nailsalon.com', 'Tanya', 'Patel', '$2a$10$deZJ/IvUf13jZXqdN2pNge9kHNZ.Llw8GlHCRURA2RnIhQtnQWeaC', '+381 64 201 4593', 'STAFF', '2025-05-31 17:23:59.397648'),
(18, '2025-05-31 17:24:18.126082', 'jasmine@nailsalon.com', 'Jasmine', 'Reed', '$2a$10$ekvnzQ1IM/T/B1T/NdQS5upRgxhd2uQkUhHxmKbf9jCypfbPlKoOG', '+381 64 201 7745', 'STAFF', '2025-05-31 17:24:18.126082'),
(19, '2025-05-31 17:24:52.481661', 'olivia@nailsalon.com', 'Olivia', 'Tran', '$2a$10$VvQTbiLp3SEKdi9MhDONl.H/tykO47erxUKK0cRtjxDHpQK2ktUhK', '+381 64 201 8832', 'STAFF', '2025-05-31 17:24:52.481661'),
(20, '2025-05-31 17:26:06.436060', 'test@gmail.com', 'Test', 'Test', '$2a$10$0KyTdvm3hp2DgEdp4arAneZF6XN9Ly9Rwztf0zkfXAZ2vYk0F4bYy', '+381 11 111 1111', 'CUSTOMER', '2025-05-31 17:26:06.436060');

-- Insert Services
INSERT INTO services (id, category, created_at, description, duration_minutes, name, price) VALUES 
(1, 'MANICURE', '2025-05-29 14:06:44.000000', 'A timeless treatment that includes nail shaping, cuticle care, a relaxing hand massage, and your choice of polish. Perfect for regular maintenance or a polished everyday look.', 30, 'Basic Manicure', 25.00),
(2, 'GEL', '2025-05-29 14:06:44.000000', 'A long-lasting, chip-resistant gel polish service that gives nails a glossy, salon-fresh look for up to two weeks. Includes shaping, buffing, and a gentle cuticle tidy-up.', 45, 'Gel Manicure', 45.00),
(3, 'PEDICURE', '2025-05-29 14:06:44.000000', 'A full set of durable acrylic extensions customized to your preferred shape and length. Finished with your choice of polish or nail art for a flawless, eye-catching result.', 45, 'Basic Pedicure', 35.00),
(4, 'NAIL_ART', '2025-05-29 14:06:44.000000', 'A creative add-on for any service, offering detailed hand-painted designs, gems, foils, or decals. Let your nails become a canvas for bold expression or subtle elegance.', 30, 'Nail Art', 15.00),
(5, 'MANICURE', '2025-05-31 17:14:15.000000', 'A relaxing manicure enhanced with essential oils, aromatic soaks, and a gentle hand massage. Includes a hot towel wrap for ultimate stress relief.', 30, 'Aromatherapy Manicure', 60.00),
(6, 'TREATMENT', '2025-05-31 17:14:59.000000', 'A holistic add-on using healing crystals like rose quartz or amethyst during your nail service. Promotes positive energy, relaxation, and mindfulness.', 60, 'Crystal Nail Healing', 75.00),
(7, 'NAIL_ART', '2025-05-31 17:16:08.000000', 'A creative session with a nail artist to design reusable, salon-quality press-ons. Ideal for events, themed looks, or frequent style changes.', 30, 'Custom Design Consultation', 20.00),
(8, 'TREATMENT', '2025-05-31 17:16:50.000000', 'A rejuvenating treatment that targets signs of aging with exfoliation, brightening masks, and collagen serums to restore youthful, glowing skin.', 45, 'Anti-Aging Hand Therapy', 50.00),
(9, 'TREATMENT', '2025-05-31 17:17:36.000000', 'A fast, restorative treatment for weak or damaged nails. Includes cuticle repair, a keratin-infused strengthening treatment, and protective coating.', 30, 'Express Nail Rehab', 40.00);

-- Insert Appointments
INSERT INTO appointments (id, appointment_date, created_at, end_time, start_time, status, customer_id, service_id, staff_id) VALUES 
(10, '2025-06-03', '2025-05-31 17:49:22.612442', '12:30:00', '12:00:00', 'COMPLETED', 20, 4, 15),
(11, '2025-06-03', '2025-05-31 21:28:13.847257', '10:30:00', '10:00:00', 'SCHEDULED', 20, 1, 16),
(12, '2025-06-05', '2025-05-31 21:29:55.447451', '16:15:00', '15:30:00', 'COMPLETED', 20, 3, 19);

Smart Healthcare Management System

This project is a Spring Boot 3 application built to help hospitals and clinics manage patients, doctors, appointments, prescriptions, and medical records in one place.
It uses both relational databases (PostgreSQL/MySQL) and MongoDB for storing data, includes JWT authentication for secure access, and applies caching to improve performance.

Authentication & Roles
Users log in with JWT tokens.
Three roles are supported:
    - Admin → manages doctors and patients.
    - Doctor → manages appointments and prescriptions.
    - Patient → books or cancels appointments and views records.

Doctor Management
    - Admins can add, edit, and remove doctors.
    - Each doctor is tied to a User account for login.
    - Doctor data is cached with Hibernate second-level cache for faster lookups.

Patient Management
    - Admins can register new patients.
    - Patients can update their details.
    - Patients also get linked User accounts for authentication.

Appointment Management
    - Patients can book and cancel appointments.
    - The system blocks double bookings for the same doctor and time slot.
    - Doctors can mark appointments as completed.
    - Booking and cancellation events are logged with Spring AOP.

Prescriptions & Records
    - Doctors can create prescriptions (stored in MongoDB).
    - Patients can view their prescription history and medical records.

Testing
Unit tests with JUnit + Mockito for:
    - Authentication services (register/update Admin, Doctor, Patient).
    - Doctor and Patient CRUD operations.

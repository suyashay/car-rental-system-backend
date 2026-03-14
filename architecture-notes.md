# System Architecture Notes

## Overview

The Car Rental Backend System follows a layered architecture using Spring Boot.
The system separates responsibilities into distinct layers to ensure maintainability and scalability.

---

# Architecture Layers

Client
↓
Controller Layer
↓
Service Layer
↓
Repository Layer
↓
MySQL Database

---

# Layer Responsibilities

### Controller Layer

Responsible for handling HTTP requests and returning responses.

Example controllers:

* AuthController
* CarController
* BookingController
* PaymentController
* DriverController

Controllers perform:

* request validation
* calling service methods
* returning API responses

---

### Service Layer

Contains all business logic.

Examples of logic implemented:

* booking validation
* driver assignment
* payment handling
* booking cancellation
* role authorization checks

The service layer ensures that rules such as booking conflicts and driver availability are enforced.

---

### Repository Layer

Responsible for interacting with the database using Spring Data JPA.

Examples:

* UserRepository
* CarRepository
* BookingRepository
* DriverRepository
* PaymentRepository

Repositories provide:

* CRUD operations
* custom queries
* pagination support

---

# Core Entities

The system is built around the following entities:

User
Car
Driver
Booking
Payment
Document

---

# Entity Relationships

User → Booking
Owner → Car
Driver → Booking
Booking → Payment
Driver → Document

These relationships allow the system to maintain connections between customers, vehicles, drivers, and payments.

---

# Booking Lifecycle

Booking creation follows a structured lifecycle.

PENDING_PAYMENT
↓
CONFIRMED
↓
COMPLETED

Alternative flow:

PENDING_PAYMENT
↓
Payment timeout
↓
CANCELLED

---

# Background Jobs

Two scheduler jobs run automatically.

## Booking Completion Job

Condition:

status = CONFIRMED
endDate ≤ today

Action:

booking.status → COMPLETED
driver.status → AVAILABLE

---

## Payment Expiration Cleanup

Condition:

status = PENDING_PAYMENT
createdAt < now - 30 minutes

Action:

booking.status → CANCELLED

---

# Deployment Architecture

The application is containerized using Docker.

Containers:

backend container → Spring Boot API
mysql container → MySQL database

Both containers run in the same Docker network and communicate using the container hostname.

Example database connection:

jdbc:mysql://mysql:3306/car_rental_db

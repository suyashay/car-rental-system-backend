# 🚗 Car Rental Backend System

A **Spring Boot backend system** for a car rental platform where customers can rent cars from owners, optionally request drivers, upload documents for verification, complete payments, and manage bookings.

The system includes **JWT authentication, role-based access control, booking conflict detection, driver allocation, payment simulation, and automated background jobs**.

This project demonstrates **real-world backend architecture and business logic implementation using Spring Boot and MySQL**, deployed using **Docker containers**.

---

# 📌 Project Overview

The Car Rental Backend System provides a platform where:

* **Customers** can search and rent cars.
* **Owners** can list and manage their vehicles.
* **Drivers** can register and get assigned to bookings.
* **Admins** can verify drivers and manage the platform.

The system ensures:

* Secure authentication using **JWT**
* **Role-based authorization**
* **Booking conflict prevention**
* **Driver assignment management**
* **Payment processing simulation**
* **Automatic booking lifecycle updates**

---

# 🧱 System Architecture

```
Client (Postman / Frontend)
        │
        ▼
Controller Layer
        │
        ▼
Service Layer
        │
        ▼
Repository Layer (Spring Data JPA)
        │
        ▼
MySQL Database
```

### Architecture Layers

| Layer      | Responsibility                 |
| ---------- | ------------------------------ |
| Controller | Exposes REST APIs              |
| Service    | Contains business logic        |
| Repository | Database interaction using JPA |
| Database   | Stores application data        |

---

# ⚙️ Tech Stack

| Technology                  | Purpose                        |
| --------------------------- | ------------------------------ |
| Java 17                     | Programming language           |
| Spring Boot                 | Backend framework              |
| Spring Security             | Authentication & authorization |
| JWT                         | Stateless authentication       |
| Spring Data JPA / Hibernate | ORM layer                      |
| MySQL                       | Relational database            |
| Docker                      | Containerized deployment       |
| Maven                       | Dependency management          |

---

# 🧩 Core Features

### 🔐 Authentication & Security

* User registration
* JWT-based login authentication
* Role-based access control
* Secure endpoints

### 👤 User Roles

| Role     | Capabilities                     |
| -------- | -------------------------------- |
| CUSTOMER | Book cars and make payments      |
| OWNER    | List and manage cars             |
| DRIVER   | Accept assigned bookings         |
| ADMIN    | Verify drivers and manage system |

---

# 🚘 Car Management

Owners can:

* Add cars to the platform
* Define car model, price per day, seats
* View bookings for their cars

Example fields:

```
Car
- id
- model
- seats
- pricePerDay
- status
- ownerId
```

---

# 📅 Booking System

Customers can book cars for specific dates.

### Booking Validation Rules

1. Only **customers can book cars**
2. End date must be after start date
3. Minimum booking duration is **1 day**
4. Car cannot be double-booked for overlapping dates

Conflict detection query:

```
findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual
```

---

# 🚖 Driver Assignment

Customers can optionally request a driver.

### Driver Allocation Logic

If `withDriver = true`:

1. System searches for an **AVAILABLE driver**
2. Driver is assigned
3. Driver status becomes **ASSIGNED**

If no driver is available:

```
Booking rejected
```

Driver becomes **AVAILABLE again** when booking completes.

---

# 💳 Payment System

Bookings must be confirmed by completing payment.

Payment states:

| Status  | Description       |
| ------- | ----------------- |
| PENDING | Payment initiated |
| SUCCESS | Payment completed |
| FAILED  | Payment failed    |

### Payment Flow

```
Booking Created
     ↓
PENDING_PAYMENT
     ↓
Payment Success
     ↓
CONFIRMED
```

---

# 📄 Driver Document Verification

Drivers must upload documents before being approved.

Example documents:

* Driving License
* Identity Proof

Document statuses:

```
PENDING
VERIFIED
REJECTED
```

Admin verifies driver documents before the driver can receive bookings.

---

# 🔄 Booking Lifecycle

The system manages bookings automatically.

```
Booking Created
        ↓
PENDING_PAYMENT
        ↓
Payment Success
        ↓
CONFIRMED
        ↓
End Date Passed
        ↓
COMPLETED
```

Alternative flow:

```
PENDING_PAYMENT
        ↓
Payment not completed within 30 minutes
        ↓
CANCELLED
```

---

# ⏱ Background Scheduler Jobs

The system includes scheduled jobs that run automatically.

### 1️⃣ Booking Completion Job

Runs periodically to update bookings.

Condition:

```
status = CONFIRMED
AND
endDate <= today
```

Action:

```
booking.status → COMPLETED
driver.status → AVAILABLE
```

---

### 2️⃣ Expired Payment Cleanup

Cancels bookings if payment is not completed within **30 minutes**.

Condition:

```
status = PENDING_PAYMENT
AND
createdAt < now() - 30 minutes
```

Action:

```
booking.status → CANCELLED
driver.status → AVAILABLE
```

---

# 🐳 Docker Deployment

The system is fully containerized.

### Containers

| Container | Purpose                 |
| --------- | ----------------------- |
| backend   | Spring Boot application |
| mysql     | MySQL database          |

### Docker Architecture

```
Docker Network
      │
      ├── Spring Boot Container
      │
      └── MySQL Container
```

---

# ▶️ Running the Project

### Clone repository

```
git clone https://github.com/<your-username>/car-rental-system-backend.git
```

```
cd backend
```

### Build application

```
mvn clean package
```

### Start containers

```
docker compose up --build
```

---

### Application runs on

```
http://localhost:8080
```

---

# 🧪 Example API Flow

### Login

```
POST /auth/login
```

Request:

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "token": "JWT_TOKEN"
}
```

---

### Create Booking

```
POST /bookings
Authorization: Bearer <JWT_TOKEN>
```

Example:

```json
{
  "carId": 1,
  "startDate": "2026-03-20",
  "endDate": "2026-03-22",
  "withDriver": true
}
```

---

### Payment

```
POST /payments
```

Example:

```json
{
  "bookingId": 1,
  "paymentMethod": "UPI"
}
```

---

# 🗄 Database Entities

Core entities in the system:

```
User
Car
Driver
Booking
Payment
Document
```

Relationships:

```
User → Booking
Owner → Car
Driver → Booking
Booking → Payment
Driver → Document
```

---

# 📈 Engineering Concepts Implemented

This project demonstrates several backend engineering concepts:

* JWT authentication
* Role-based access control
* Transaction management
* Booking conflict prevention
* Driver allocation logic
* Payment lifecycle handling
* Background schedulers
* Database relationships
* Docker containerization

---

# 📚 Future Improvements

Possible enhancements:

* Real payment gateway integration
* Frontend client application
* Redis caching
* Rate limiting
* API documentation using Swagger
* Kubernetes deployment

---

# 👨‍💻 Author

Developed as a backend engineering project using **Spring Boot, MySQL, and Docker** to demonstrate scalable system design and production-style backend architecture.

---

**🚗 Car Rental Backend System**

A Spring Boot backend system for a car rental platform where customers can rent cars, request drivers, manage bookings, and complete payments.

The system includes JWT authentication, role-based access control, booking conflict detection, driver allocation, and automated background jobs, and is fully deployed using Docker on AWS EC2.

---

**🚀 Live API**

Health Check:

```
http://54.226.198.22:8080/health
```

Response:
```
Car Rental Backend Running
```

---

🔐 Authentication Example

Login
```
POST /auth/login
```

Request:
```
{
  "email": "test@example.com",
  "password": "password123"
}
```

Response:
```
{
  "token": "JWT_TOKEN"
}
```

Use token:
```
Authorization: Bearer <JWT_TOKEN>
```

---

**⚡ Quick Start**

1. Clone repository

```
git clone https://github.com/<your-username>/car-rental-system-backend.git
cd backend
```

2. Configure environment variables

Create .env file:
```
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=car_rental_db
MYSQL_USER=root
MYSQL_PASSWORD=root
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/car_rental_db
```

3. Run application

```
mvn clean package -DskipTests
docker compose up --build
```

4. Access API

```
http://localhost:8080
```

---

**📌 Project Overview**

The system supports:
- Customers → book cars
- Owners → manage cars
- Drivers → get assigned to bookings
- Admins → verify and manage system

Core guarantees:
- Secure JWT authentication
- Role-based authorization
- Booking conflict prevention
- Driver assignment logic
- Payment lifecycle handling
- Automated booking updates

---

**🧱 System Architecture**

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

---

**⚙️ Tech Stack**

Technology	Purpose
Java 17	        Programming language
Spring Boot	Backend framework
Spring Security	Authentication & authorization
JWT	        Stateless authentication
Spring Data JPA	ORM layer
MySQL	        Database
Docker	        Containerization
AWS EC2	        Deployment

---

**🧩 Core Features**

🔐 Authentication
- User registration & login
- JWT-based authentication
- Role-based access control

🚘 Car Management
- Owners add/manage cars
- Pricing per day
- Booking visibility

📅 Booking System
Rules enforced:
1.Only customers can book
2.End date must be after start date
3.Minimum 1-day booking
4.No overlapping bookings

🚖 Driver Assignment
If withDriver = true:
- Assign first AVAILABLE driver
- Driver → ASSIGNED
- On completion → AVAILABLE

If no driver:
Booking rejected

💳 Payment Flow
Booking Created
      ↓
PENDING_PAYMENT
      ↓
SUCCESS
      ↓
CONFIRMED

🔄 Booking Lifecycle
CONFIRMED → COMPLETED (after end date)
PENDING_PAYMENT → CANCELLED (after 30 mins)

---

**⏱ Background Jobs**

1. Completion Job

Condition:

status = CONFIRMED AND endDate <= today

2. Payment Expiry Job

Condition:

status = PENDING_PAYMENT AND createdAt < now() - 30 min

---

**🐳 Docker Deployment**

Containers

Container	Purpose
backend	        Spring Boot app
mysql	        Database

---

**⚠️ Error Handling**

Example:

{
  "message": "Car not found",
  "status": 400,
  "time": "2026-03-17T12:30:00"
}

---

📊 Request Logging

Example:

POST /auth/login | Status: 200 | Time: 45ms
GET /bookings/my | Status: 401 | Time: 12ms

---

🧪 Example API

Create Booking

POST /bookings
Authorization: Bearer <JWT_TOKEN>

{
  "carId": 1,
  "startDate": "2026-03-20",
  "endDate": "2026-03-22",
  "withDriver": true
}

🗄 Database Entities

User
Car
Driver
Booking
Payment
Document

---

**📈 Engineering Concepts**

- JWT Authentication
- RBAC (Role-Based Access Control)
- Transaction Management
- Scheduler Jobs
- Conflict Detection
- Dockerized Deployment

---

**📚 Future Improvements**

- Payment gateway integration
- Frontend (React)
- Redis caching
- Rate limiting
- Swagger docs
- Kubernetes deployment

---

**👨‍💻 Author**

Developed as a backend engineering project using *Spring Boot, MySQL, and Docker* to demonstrate scalable system design and production-style backend architecture.
- Suyasha Yerram

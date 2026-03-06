Title -
Car Rental Backend System

Problem -
People should be able to rent cars directly from owners through a platform.

Actors -
Owner
Renter
Admin

Core flows -
owner lists vehicle
renter searches cars
renter books car
documents verified
payment processed
booking lifecycle tracked

Engineering focus -
booking conflict prevention
transactional consistency
role-based system

Tech stack -
Spring Boot
MySQL
JPA
Docker (planned)
AWS (planned)


User Management
- User registration with role assignment
- Password hashing using BCrypt

Vehicle Management
- Owners can add cars to the platform
- Vehicle filtering by seat count and availability

Booking Engine
- Customers can book cars for a date range
- Overlapping bookings are prevented using interval logic

Booking Lifecycle
- Booking creation
- Booking cancellation with authorization
- Automatic completion based on checkout date

Validation Rules
- Cannot book past dates
- Minimum booking duration = 1 day
- Only booking owner or admin can cancel

Booking States -
CONFIRMED → CANCELLED
CONFIRMED → COMPLETED
Rules:
- Only CONFIRMED bookings can be cancelled
- Bookings automatically move to COMPLETED when
  current date >= endDate

Date Model -
startDate = pickup date
endDate = checkout date (exclusive)
Example:

Start: April 1
End: April 4

Car used on:
April 1, 2, 3

Return date:
April 4

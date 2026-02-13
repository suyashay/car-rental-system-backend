Entities:

User
Car
Booking
Availability
Document
Payment

📌 SECTION 1 — Booking Conflict Logic

1 Problem

A car must not be double-booked for overlapping dates.

Example 1:
Existing booking:
Start: 10 Jan
End: 15 Jan

New booking:
Start: 12 Jan
End: 18 Jan

These overlap → reject.

Example 2:
Existing booking:
start: 10 Jan 
end: 15 Jan

New booking:
start: 16 Jan
end: 20 Jan

No overlap → allow.


2 Overlap Condition

Two bookings overlap if:
existing.startDate <= new.endDate
AND
existing.endDate >= new.startDate

Why this works:
If existing starts before new ends AND existing ends after new starts
Then they share at least one day.

This condition covers all cases:
full overlap
partial overlap
boundary overlap


3 JPA Query Design

query:
SELECT b FROM Booking b
WHERE b.car.id = :carId
AND b.startDate <= :newEndDate
AND b.endDate >= :newStartDate

If this query returns any record → conflict exists.


📌 SECTION 2 — Transaction Boundary Design

Problem:
Booking creation involves multiple steps:
Create booking
Create payment
Update booking status

If payment succeeds but booking update fails,
system becomes inconsistent.

Example:
Payment = SUCCESS
Booking = not saved

Money taken, no booking record.

That is data corruption.

Solution:
Booking + Payment must run inside one transaction.

Meaning:
If any step fails
Entire operation rolls back

Using:
@Transactional

Ensures atomicity.

Atomicity = All succeed or none succeed.


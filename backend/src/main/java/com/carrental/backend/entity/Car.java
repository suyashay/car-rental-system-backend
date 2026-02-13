package com.carrental.backend.entity;

import com.carrental.backend.entity.enums.CarStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String model;

    private int seats;

    private double pricePerDay;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id",  nullable = false)
    private User owner;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private java.util.List<Booking> bookings;

}

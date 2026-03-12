package com.carrental.backend.entity;

import com.carrental.backend.entity.enums.DriverStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String phone;

    private String carNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Booking> bookings;

}

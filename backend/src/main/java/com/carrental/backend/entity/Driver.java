package com.carrental.backend.entity;

import com.carrental.backend.entity.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

}

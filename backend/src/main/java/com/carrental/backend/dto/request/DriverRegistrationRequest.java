package com.carrental.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverRegistrationRequest {

    private String name;
    private String phone;
    private String carNumber;
}

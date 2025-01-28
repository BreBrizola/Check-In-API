package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DriversLicenseDTO implements Serializable {
    private Long id;
    private String countryCode;
    private String countrySubdivision;
    private LocalDate licenseExpirationDate;
    private String licenseNumber;
}

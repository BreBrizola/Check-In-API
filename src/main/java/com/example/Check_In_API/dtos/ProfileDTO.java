package com.example.Check_In_API.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ProfileDTO implements Serializable {
    private String loyaltyNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private AddressDTO addressEntity;

    private DriversLicenseDTO driversLicenseEntity;

    private LoginDTO login;
}

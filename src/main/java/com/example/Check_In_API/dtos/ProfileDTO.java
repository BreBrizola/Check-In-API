package com.example.Check_In_API.dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProfileDTO implements Serializable {
    private String loyaltyNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String dateOfBirth;

    private AddressDTO address;

    private DriversLicenseDTO driversLicense;

    private LoginDTO login;
}

package com.example.Check_In_API.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

@Data
public class ProfileDTO implements Serializable {
    private String loyaltyNumber;

    @NotEmpty(message = "First name is required")
    @Size(min = 1, max = 30)
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(min = 1, max = 80)
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Size(min = 1, max = 100)
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Size(min = 11, max = 20)
    private String phone;

    @NotEmpty(message = "Date of birth is required")
    @Size(min = 8, max = 10)
    private String dateOfBirth;

    private AddressDTO address;

    private DriversLicenseDTO driversLicense;

    private LoginDTO login;

    private boolean found;
}

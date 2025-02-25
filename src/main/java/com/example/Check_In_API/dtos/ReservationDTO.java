package com.example.Check_In_API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationDTO implements Serializable {
    private Long id;

    private String confirmationNumber;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 30)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 80)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(min = 1, max = 100)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 20, max = 20)
    private String phone;

    private double totalPrice;

    private LocalDate pickupDate;

    private LocalDate returnDate;

    private String pickupTime;

    private String returnTime;

    private ProfileDTO profile;

    private LocationDTO pickupLocation;

    private LocationDTO returnLocation;

    private VehicleDTO vehicle;

    private List<AdditionalProductDTO> additionalProducts;
}

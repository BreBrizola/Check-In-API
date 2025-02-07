package com.example.Check_In_API.dtos;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReservationDTO implements Serializable {
    private Long id;

    private String confirmationNumber;

    private String firstName;

    private String lastName;

    private String email;

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

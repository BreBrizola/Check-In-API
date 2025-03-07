package com.example.Check_In_API.dtos;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class LocationDTO implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String openingHours;
    private Long afterHoursFee;
    private List<LocationTermsDTO> locationTerms;
}

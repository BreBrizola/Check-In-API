package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDTO implements Serializable {
    private Long id;
    private String city;
    private String country;
    private String countrySubdivisionCode;
    private String postal;
    private String streetAddresses;
}

package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleTermsDTO implements Serializable {
    @JsonProperty("id")private Long id;

    @JsonProperty("vehicle")private VehicleDTO vehicle;

    @JsonProperty("terms")private TermsDTO terms;
}

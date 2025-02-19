package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LocationTermsDTO implements Serializable {
    @JsonProperty("id")private Long id;

    @JsonProperty("location")private LocationDTO location;

    @JsonProperty("terms")private TermsDTO terms;
}

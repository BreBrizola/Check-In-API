package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class VehicleDTO implements Serializable {
   private long id;
   private String model;
   private double price;
   private List<VehicleTermsDTO> vehicleTerms;
}


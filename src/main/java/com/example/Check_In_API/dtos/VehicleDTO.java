package com.example.Check_In_API.dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class VehicleDTO implements Serializable {
   private long id;
   private String model;
   private double price;
}


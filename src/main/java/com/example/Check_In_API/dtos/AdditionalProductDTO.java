package com.example.Check_In_API.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdditionalProductDTO implements Serializable {
    private Long id;
    private String name;
    private double price;
}

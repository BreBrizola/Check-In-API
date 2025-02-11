package com.example.Check_In_API.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.io.Serializable;

@Data
@JsonSerialize(as = Session.class)
public class Session implements Serializable {
    private ReservationDTO reservation;
    private ProfileDTO profile;
}


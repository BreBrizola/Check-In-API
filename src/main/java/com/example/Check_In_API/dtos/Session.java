package com.example.Check_In_API.dtos;

import com.example.Check_In_API.enums.CheckInRedirectEnum;
import lombok.Data;

@Data
public class Session {
    private ReservationDTO reservation;

    public Session(ReservationDTO reservation) {
        this.reservation = reservation;
    }
}


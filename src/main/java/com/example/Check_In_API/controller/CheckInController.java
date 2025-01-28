package com.example.Check_In_API.controller;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ReservationDTO;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private CarRentalRetroFitClient carRentalRetroFitClient;

    public CheckInController(CarRentalRetroFitClient carRentalRetroFitClient){
        this.carRentalRetroFitClient = carRentalRetroFitClient;
    }

    @GetMapping("/retrieve")
    public Observable<ReservationDTO> getReservation(@RequestParam String confirmationNumber, String firstName, String lastName){
        return carRentalRetroFitClient.getReservation(confirmationNumber, firstName, lastName);
    }
}

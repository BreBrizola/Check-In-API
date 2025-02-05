package com.example.Check_In_API.controller;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.PROFILE_SEARCH;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.START;

@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private CarRentalRetroFitClient carRentalRetroFitClient;
    private CheckInService checkInService;

    public CheckInController(CarRentalRetroFitClient carRentalRetroFitClient, CheckInService checkInService){
        this.carRentalRetroFitClient = carRentalRetroFitClient;
        this.checkInService = checkInService;
    }

    @GetMapping("/retrieve")
    public Observable<RedirectResponse> getReservation(@RequestParam String confirmationNumber, String firstName, String lastName){
        return checkInService.getReservation(confirmationNumber, firstName, lastName)
                .map(reservation -> new RedirectResponse(new Session(reservation), START));
    }

    @GetMapping("/profile_search")
    public Observable<RedirectResponse> redirectToProfileSearch(@RequestParam String confirmationNumber, String firstName, String lastName){
        return checkInService.getReservation(confirmationNumber, firstName, lastName)
                .map(reservation -> new RedirectResponse(new Session(reservation), PROFILE_SEARCH));
    } //Quando for usar o Session do Redis da pra pegar os dados da sessao sem ter que passar os parametros de novo
}

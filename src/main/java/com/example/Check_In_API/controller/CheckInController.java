package com.example.Check_In_API.controller;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.PROFILE_SEARCH;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.START;

@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private CheckInService checkInService;

    private Session session;
//
    public CheckInController(CheckInService checkInService, Session session){
        this.checkInService = checkInService;
        this.session = session;
    }

    @GetMapping("/retrieve")
    public Observable<RedirectResponse> getReservation(@RequestParam String confirmationNumber, String firstName, String lastName) {
        return checkInService.getReservation(confirmationNumber, firstName, lastName)
                .map(session -> new RedirectResponse(session, START));
    }

    @GetMapping("/redirect_profile_search")
    public Observable<RedirectResponse> redirectToProfileSearch() {
        return Observable.just(new RedirectResponse(session ,PROFILE_SEARCH));
    }

    @GetMapping("/profile_search")
    public Maybe<RedirectResponse> searchProfile(@RequestParam String driversLicenseNumber, @RequestParam String lastName, @RequestParam String issuingCountry, @RequestParam String issuingAuthority){
        return checkInService.searchUserProfile(driversLicenseNumber, lastName, issuingCountry, issuingAuthority);
    }

    @PostMapping("/driver_details")
    public Observable<RedirectResponse> driverDetails(@RequestBody ProfileDTO updatedProfile){
        return checkInService.updateDriverDetails(updatedProfile);
    }

    @PostMapping("/create_profile")
    public Observable<RedirectResponse> createProfile(@RequestBody ProfileDTO profile){
        return checkInService.createProfile(profile);
    }

    @PostMapping("/reservation_details")
    public Observable<RedirectResponse> reservationDetails(@RequestBody ReservationDTO reservation){
        return checkInService.reservationDetails(reservation);
    }
}

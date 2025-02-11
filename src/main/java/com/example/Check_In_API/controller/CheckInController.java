package com.example.Check_In_API.controller;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.PROFILE_SEARCH;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.START;

@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private CheckInService checkInService;

    public CheckInController(CheckInService checkInService){
        this.checkInService = checkInService;
    }

    @GetMapping("/retrieve")
    public Observable<RedirectResponse> getReservation(@RequestParam String confirmationNumber, String firstName, String lastName) {
        return checkInService.getReservation(confirmationNumber, firstName, lastName)
                .map(session -> new RedirectResponse(session, START));
    }

    @GetMapping("/redirect_profile_search")
    public Observable<RedirectResponse> redirectToProfileSearch() {
        return Observable.just(new RedirectResponse(checkInService.getSession(),PROFILE_SEARCH));
    }

    @GetMapping("/profile_search")
    public Observable<ProfileDTO> searchProfile(@RequestParam String driversLicenseNumber, @RequestParam String lastName, @RequestParam String issuingCountry, @RequestParam String issuingAuthority){
        return checkInService.searchUserProfile(driversLicenseNumber, lastName, issuingCountry, issuingAuthority);
    }
}//tentar fazer o redirect response retornar um profile tbm

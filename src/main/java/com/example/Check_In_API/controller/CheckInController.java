package com.example.Check_In_API.controller;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.dtos.TermsDTO;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.CONFIRMATION;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.PROFILE_SEARCH;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.START;

@RestController
@RequestMapping("checkIn")
public class CheckInController {

    private CheckInService checkInService;

    private Session session;

    public CheckInController(CheckInService checkInService, Session session){
        this.checkInService = checkInService;
        this.session = session;
    }

    @Operation(summary = "Retrieve reservation details",
            description = "Pass the confirmation number, first name, and last name to retrieve the reservation.")
    @GetMapping("/retrieve")
    public Observable<RedirectResponse> getReservation(@RequestParam String confirmationNumber, String firstName, String lastName) {
        return checkInService.getReservation(confirmationNumber, firstName, lastName)
                .map(session -> new RedirectResponse(session, START));
    }

    @Operation(summary = "Redirect to profile search",
            description = "Redirects the user to the profile search screen.")
    @GetMapping("/redirect_profile_search")
    public Observable<RedirectResponse> redirectToProfileSearch() {
        return Observable.just(new RedirectResponse(session ,PROFILE_SEARCH));
    }

    @Operation(summary = "Search for a user profile",
            description = "Pass the driver's license number, last name, issuing country, and issuing authority to search for a profile.")
    @GetMapping("/profile_search")
    public Maybe<RedirectResponse> searchProfile(@RequestParam String driversLicenseNumber, @RequestParam String lastName, @RequestParam String issuingCountry, @RequestParam String issuingAuthority){
        return checkInService.searchUserProfile(driversLicenseNumber, lastName, issuingCountry, issuingAuthority);
    }

    @Operation(summary = "Update driver details",
            description = "Pass the updated driver profile details.")
    @PostMapping("/driver_details")
    public Observable<RedirectResponse> driverDetails(@RequestBody ProfileDTO updatedProfile){
        return checkInService.updateDriverDetails(updatedProfile);
    }

    @Operation(summary = "Create a new profile",
            description = "Pass the necessary information to create a new driver profile.")
    @PostMapping("/create_profile")
    public Observable<RedirectResponse> createProfile(@RequestBody ProfileDTO profile){
        return checkInService.createProfile(profile);
    }

    @Operation(summary = "Submit reservation details",
            description = "Pass the reservation details to proceed with the check-in process.")
    @PostMapping("/reservation_details")
    public Observable<RedirectResponse> reservationDetails(@RequestBody ReservationDTO reservation){
        return checkInService.reservationDetails(reservation);
    }

    @Operation(summary = "Retrieve vehicle terms",
            description = "Retrieve the terms and conditions related to the session selected vehicle.")
    @GetMapping("/vehicle_terms")
    public Observable<List<TermsDTO>> vehicleTerms(){
        return checkInService.getVehicleTerms();
    }

    @Operation(summary = "Retrieve location terms",
            description = "Retrieve the terms and conditions related to the session selected location.")
    @GetMapping("/location_terms")
    public Observable<List<TermsDTO>> locationTerms(){
        return checkInService.getLocationTerms();
    }

    @Operation(summary = "Redirect to confirmation",
            description = "Redirects the user to the confirmation screen.")
    @GetMapping("/redirect_confirmation")
    public Observable<RedirectResponse> redirectToConfirmation() {
        return Observable.just(new RedirectResponse(session ,CONFIRMATION));
    }

    @Operation(summary = "Confirm reservation",
            description = "Retrieve the final reservation details before confirmation.")
    @GetMapping("/confirmation")
    public Observable<ReservationDTO> confirmation(){
        return checkInService.confirmation();
    }
}

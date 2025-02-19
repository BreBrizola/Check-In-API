package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.dtos.TermsDTO;
import com.example.Check_In_API.dtos.VehicleDTO;
import com.example.Check_In_API.enums.CheckInRedirectEnum;
import com.example.Check_In_API.exception.ReservationNotEligibleForCheckInException;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.CREATE_PROFILE;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.DRIVER_DETAILS;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.RESERVATION_DETAILS;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.TERMS;

@Service
public class CheckInService {

    private final CarRentalRetroFitClient carRentalRetroFitClient;

    private Session session;

    public CheckInService(CarRentalRetroFitClient carRentalRetroFitClient, Session session) {
        this.carRentalRetroFitClient = carRentalRetroFitClient;
        this.session = session;
    }

    public Observable<Session> getReservation(String confirmationNumber, String firstName, String lastName) {
        return carRentalRetroFitClient.getReservation(confirmationNumber, firstName, lastName)
                .flatMap(reservation -> {
                        isEligibleForCheckIn(reservation.getPickupDate(), reservation.getPickupTime());
                        session.setReservation(reservation);
                        return Observable.just(session);
                })
                .onErrorResumeNext((Function<Throwable, Observable<Session>>) throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        if (httpException.code() == 404) {
                            return Observable.error(new ReservationNotFoundException("Reservation not found"));
                        }
                    }
                    return Observable.error(throwable);
                });
    }

    public Maybe<RedirectResponse> searchUserProfile(String driversLicenseNumber, String lastName, String issuingCountry, String issuingAuthority){
        return carRentalRetroFitClient.searchUserProfile(driversLicenseNumber, lastName, issuingCountry, issuingAuthority)
                .map(profile -> {
                    CheckInRedirectEnum redirect;
                    session.setProfile(null);

                    if (profile.isFound()) {
                        session.setProfile(profile);
                        redirect = DRIVER_DETAILS;
                    } else {
                        redirect = CREATE_PROFILE;
                    }

                    return new RedirectResponse(session, redirect);
                });
    }

    public Observable<RedirectResponse> createProfile(ProfileDTO profile){
        return carRentalRetroFitClient.submitPersonalInformation(profile)
                .map(response -> {
                    session.setProfile(response);
                    session.getReservation().setProfile(response);

                    return new RedirectResponse(session, RESERVATION_DETAILS);
                });
    }

    public Observable<RedirectResponse> updateDriverDetails(ProfileDTO updatedProfile){
        String loyaltyNumber = session.getProfile().getLoyaltyNumber();
        ReservationDTO reservation = session.getReservation();

        return carRentalRetroFitClient.editProfile(loyaltyNumber, updatedProfile)
                .flatMap(response -> {
                    reservation.setProfile(response);
                    return carRentalRetroFitClient.updateReservation(
                            reservation.getConfirmationNumber(),
                            reservation.getFirstName(),
                            reservation.getLastName(),
                            reservation
                    );
                })
                .map(updateResponse -> new RedirectResponse(session, RESERVATION_DETAILS));
    }

    public Observable<RedirectResponse> reservationDetails(ReservationDTO updatedReservation) {
                    ReservationDTO existingReservation = session.getReservation();

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    LocalTime oldPickupTime = LocalTime.parse(existingReservation.getPickupTime(), dateTimeFormatter);
                    LocalTime newPickTime = LocalTime.parse(updatedReservation.getPickupTime(), dateTimeFormatter);

                    if(newPickTime.isBefore(oldPickupTime.minusHours(4)) || newPickTime.isAfter(oldPickupTime.plusHours(4))){
                        return Observable.error(new IllegalArgumentException("PickUp time can only be altered in +4 or -4 hours from the original time."));
                    }

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime pickUpDateTime = LocalDateTime.of(existingReservation.getPickupDate(), newPickTime);

                    if(pickUpDateTime.isBefore(now)){
                        return Observable.error(new IllegalArgumentException("PickUp time can't be in the past."));
                    }

                    existingReservation.setPickupTime(updatedReservation.getPickupTime());

                    session.setReservation(existingReservation);

                    return carRentalRetroFitClient.updateReservation(existingReservation.getConfirmationNumber(), existingReservation.getFirstName(),
                                                              existingReservation.getLastName(), existingReservation)
                            .ignoreElements()
                            .andThen(Observable.just(new RedirectResponse(session, TERMS)));
    }

    public Observable<List<TermsDTO>> getVehicleTerms(){
        return carRentalRetroFitClient.getVehicleTerms(session.getReservation().getVehicle().getId())
                .map(terms -> {
                    List<TermsDTO> activeTerms = new ArrayList<>();
                    for(TermsDTO term : terms){
                        if(term.isActive()){
                            activeTerms.add(term);
                        }
                    }

                    return activeTerms;
    });
    }

    public Observable<List<TermsDTO>> getLocationTerms(){
        return carRentalRetroFitClient.getLocationTerms(session.getReservation().getPickupLocation().getId())
                .map(terms -> {
                    List<TermsDTO> activeTerms = new ArrayList<>();
                    for(TermsDTO term : terms){
                        if(term.isActive()){
                            activeTerms.add(term);
                        }
                    }

                    return activeTerms;
                });
    }

    public void isEligibleForCheckIn(LocalDate pickupDate, String pickupTime) {
        LocalTime pickupLocalTime = LocalTime.parse(pickupTime);
        LocalDateTime pickupDateTime = LocalDateTime.of(pickupDate, pickupLocalTime);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInStart = pickupDateTime.minusHours(24);

        if(!(now.isAfter(checkInStart) && now.isBefore(pickupDateTime))){
            throw new ReservationNotEligibleForCheckInException("Reservation is not eligible for check-in.");
        }
    }
}

package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.enums.CheckInRedirectEnum;
import com.example.Check_In_API.exception.ReservationNotEligibleForCheckInException;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import lombok.Getter;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.CREATE_PROFILE;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.DRIVER_DETAILS;
import static com.example.Check_In_API.enums.CheckInRedirectEnum.RESERVATION_DETAILS;

@Service
public class CheckInService {

    private final CarRentalRetroFitClient carRentalRetroFitClient;

    @Getter
    private Session session;

    public CheckInService(CarRentalRetroFitClient carRentalRetroFitClient, Session session) {
        this.carRentalRetroFitClient = carRentalRetroFitClient;
        this.session = session;
    }

    public Observable<Session> getReservation(String confirmationNumber, String firstName, String lastName) {
        return carRentalRetroFitClient.getReservation(confirmationNumber, firstName, lastName)
                .flatMap(reservation -> {
                    try {
                        isEligibleForCheckIn(reservation.getPickupDate(), reservation.getPickupTime());
                        session.setReservation(reservation);
                        return Observable.just(session);
                    } catch (ReservationNotEligibleForCheckInException ex) {
                        return Observable.error(ex);
                    }
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

                    if (profile.isFound()) {
                        session.setProfile(profile);
                        redirect = DRIVER_DETAILS;
                    } else {
                        session = new Session();
                        redirect = CREATE_PROFILE;
                    }

                    return new RedirectResponse(session, redirect);
                });
    }

    public Observable<RedirectResponse> updateDriverDetails(ProfileDTO updatedProfile){
        String loyaltyNumber = session.getProfile().getLoyaltyNumber();
        ReservationDTO reservation = session.getReservation();

        return carRentalRetroFitClient.editProfile(loyaltyNumber, updatedProfile)
                .flatMap(response -> {
                    reservation.setProfile(updatedProfile);
                    return carRentalRetroFitClient.updateReservation(
                            reservation.getConfirmationNumber(),
                            reservation.getFirstName(),
                            reservation.getLastName(),
                            reservation
                    );
                })
                .map(updateResponse -> new RedirectResponse(session, RESERVATION_DETAILS));
    }
    /*
    public Observable<RedirectResponse> updateDriverDetails(ProfileDTO updatedProfile){
        return Observable.fromCallable(() -> {
            ProfileDTO profile = session.getProfile();

            LocalDate updatedExpirationDate = LocalDate.parse(updatedProfile.getDriversLicense().getLicenseExpirationDate());
            if (updatedExpirationDate.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Driver license expiration date cannot be in the past");
            }

            String expirationDate = updatedExpirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            profile.getDriversLicense().setLicenseExpirationDate(expirationDate);
            profile.setAddress(updatedProfile.getAddress());
            profile.setPhone(updatedProfile.getPhone());

            ReservationDTO reservation = session.getReservation();

            reservation.setProfile(profile);

            return new RedirectResponse(session, RESERVATION_DETAILS);
        });
    }
     */

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

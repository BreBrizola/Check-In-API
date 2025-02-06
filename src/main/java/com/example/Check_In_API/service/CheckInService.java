package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.exception.ReservationNotEligibleForCheckInException;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import lombok.Getter;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class CheckInService {

    private final CarRentalRetroFitClient carRentalRetroFitClient;

    @Getter
    private Session session;

    public CheckInService(CarRentalRetroFitClient carRentalRetroFitClient, Session session){
        this.carRentalRetroFitClient = carRentalRetroFitClient;
        this.session = session;
    }

    public Observable<Session> getReservation(String confirmationNumber, String firstName, String lastName){
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

    public void isEligibleForCheckIn(LocalDate pickupDate, String pickupTime){
        LocalTime pickupLocalTime = LocalTime.parse(pickupTime);
        LocalDateTime pickupDateTime = LocalDateTime.of(pickupDate, pickupLocalTime);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInStart = pickupDateTime.minusHours(24);

        if(!(now.isAfter(checkInStart) && now.isBefore(pickupDateTime))){
            throw new ReservationNotEligibleForCheckInException("Reservation is not eligible for check-in.");
        }
    }
}

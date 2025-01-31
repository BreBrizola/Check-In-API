package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

@Service
public class CheckInService {

    private final CarRentalRetroFitClient carRentalRetroFitClient;

    public CheckInService(CarRentalRetroFitClient carRentalRetroFitClient){
        this.carRentalRetroFitClient = carRentalRetroFitClient;
    }

    public Observable<ReservationDTO> getReservation(String confirmationNumber, String firstName, String lastName){
        return carRentalRetroFitClient.getReservation(confirmationNumber, firstName, lastName)
                .onErrorResumeNext((Function<Throwable, Observable<ReservationDTO>>) throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        if (httpException.code() == 404) {
                            return Observable.error(new ReservationNotFoundException("Reservation not found"));
                        }
                    }
                    return Observable.error(throwable);
                });
    }
}

package com.example.Check_In_API.client;

import com.example.Check_In_API.dtos.ReservationDTO;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarRentalRetroFitClient {
    @GET("/reservation/retrieve")
    Observable<ReservationDTO> getReservation(@Query("confirmationNumber") String confirmationNumber,
                                              @Query ("firstName") String firstName,
                                              @Query ("lastName") String lastName);
}

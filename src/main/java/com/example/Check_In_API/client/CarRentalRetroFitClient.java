package com.example.Check_In_API.client;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.ReservationDTO;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface CarRentalRetroFitClient {
    @GET("/reservation/retrieve")
    Observable<ReservationDTO> getReservation(@Query("confirmationNumber") String confirmationNumber,
                                              @Query ("firstName") String firstName,
                                              @Query ("lastName") String lastName);

    @GET("/enroll/profileSearch")
    Maybe<ProfileDTO> searchUserProfile(@Query ("driversLicenseNumber") String driversLicenseNumber,
                                        @Query ("lastName") String lastName,
                                        @Query ("issuingCountry") String issuingCountry,
                                        @Query ("issuingAuthority") String issuingAuthority);
}

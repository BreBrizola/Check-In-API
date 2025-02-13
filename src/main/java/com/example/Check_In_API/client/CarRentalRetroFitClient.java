package com.example.Check_In_API.client;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.ReservationDTO;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Path;

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

    @PUT("/enroll/editProfile")
    Observable<ProfileDTO> editProfile(@Query("loyaltyNumber") String loyaltyNumber, @Body ProfileDTO updatedProfile);

    @PUT("/reservation/update/{confirmationNumber}")
    Observable<ReservationDTO> updateReservation(@Path("confirmationNumber") String confirmationNumber,
                                                 @Query("firstName") String firstName,
                                                 @Query("lastName") String lastName,
                                                 @Body ReservationDTO updatedReservation);
}

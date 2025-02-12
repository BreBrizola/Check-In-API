package com.example.Check_In_API.client;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.ReservationDTO;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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
    Observable<ProfileDTO> editProfile (@Query("loyaltyNumber") String loyaltyNumber, @Query("profile") ProfileDTO updatedProfile);

    @PUT("/reservation/update/{confirmationNumber}")
    Observable<ReservationDTO> updateReservation (@Query("confirmationNumber") String confirmationNumber, @Query("firstName") String firstName,
                                                  @Query("lastName") String lastName, @Query("reservation") ReservationDTO updatedReservation);

}

package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.exception.ReservationNotEligibleForCheckInException;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.HttpException;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.Month;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {
    @InjectMocks
    private CheckInService checkInService;

    @Mock
    private CarRentalRetroFitClient carRentalRetroFitClient;

    @Mock
    private Session session;

    @Test
    public void givenReservation_whenReservationIsNotEligibleForCheckin_thenThrowException() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setPickupDate(LocalDate.of(2025, Month.JANUARY,1));
        reservation.setPickupTime("16:00");

        when(carRentalRetroFitClient.getReservation(anyString(), anyString(), anyString())).thenReturn(Observable.just(reservation));

        assertThrows(ReservationNotEligibleForCheckInException.class, () -> checkInService.getReservation(anyString(), anyString(), anyString()).blockingFirst());
    }

    @Test
    public void givenReservation_whenReservationNotFound_thenThrowException() {
        when(carRentalRetroFitClient.getReservation(anyString(), anyString(), anyString())).thenThrow(new ReservationNotFoundException("reservation not found"));

        assertThrows(ReservationNotFoundException.class, () -> checkInService.getReservation(anyString(), anyString(), anyString()).blockingFirst());
    }
}
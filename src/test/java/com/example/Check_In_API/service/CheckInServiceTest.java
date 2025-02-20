package com.example.Check_In_API.service;

import com.example.Check_In_API.client.CarRentalRetroFitClient;
import com.example.Check_In_API.dtos.LocationDTO;
import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.dtos.TermsDTO;
import com.example.Check_In_API.dtos.VehicleDTO;
import com.example.Check_In_API.exception.ReservationNotEligibleForCheckInException;
import com.example.Check_In_API.exception.ReservationNotFoundException;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static com.example.Check_In_API.enums.CheckInRedirectEnum.RESERVATION_DETAILS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckInServiceTest {
    @InjectMocks
    private CheckInService checkInService;

    @Mock
    private CarRentalRetroFitClient carRentalRetroFitClient;

    @Mock
    private Session session;

    @Mock
    private ReservationDTO reservation;

    @Test
    public void givenReservation_whenReservationIsNotEligibleForCheckin_thenThrowException() {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setPickupDate(LocalDate.of(2025, Month.JANUARY,1));
        reservation.setPickupTime("16:00");

        when(carRentalRetroFitClient.getReservation(anyString(), anyString(), anyString())).thenReturn(Observable.just(reservation));

        Assertions.assertThrows(ReservationNotEligibleForCheckInException.class, () -> checkInService.getReservation(anyString(), anyString(), anyString()).blockingFirst());
    }

    @Test
    public void givenReservation_whenReservationNotFound_thenThrowException() {
        when(carRentalRetroFitClient.getReservation(anyString(), anyString(), anyString())).thenThrow(new ReservationNotFoundException("reservation not found"));

        Assertions.assertThrows(ReservationNotFoundException.class, () -> checkInService.getReservation(anyString(), anyString(), anyString()).blockingFirst());
    }

    @Test
    public void givenVehicleTerms_whenActiveTermsExist_thenReturnOnlyActiveTerms() {
        VehicleDTO vehicle = mock(VehicleDTO.class);
        when(session.getReservation()).thenReturn(reservation);
        when(reservation.getVehicle()).thenReturn(vehicle);
        when(vehicle.getId()).thenReturn(1L);

        TermsDTO term1 = new TermsDTO();
        term1.setActive(true);
        TermsDTO term2 = new TermsDTO();
        term2.setActive(false);
        TermsDTO term3 = new TermsDTO();
        term3.setActive(true);

        List<TermsDTO> terms = Arrays.asList(
                term1, term2, term3
        );

        when(carRentalRetroFitClient.getVehicleTerms(1L)).thenReturn(Observable.just(terms));

        List<TermsDTO> activeTerms = checkInService.getVehicleTerms().blockingFirst();

        assertEquals(2, activeTerms.size());
    }

    @Test
    public void givenLocationTerms_whenActiveTermsExist_thenReturnOnlyActiveTerms() {
        LocationDTO location = mock(LocationDTO.class);
        when(session.getReservation()).thenReturn(reservation);
        when(reservation.getPickupLocation()).thenReturn(location);
        when(location.getId()).thenReturn(1L);

        TermsDTO term1 = new TermsDTO();
        term1.setActive(true);
        TermsDTO term2 = new TermsDTO();
        term2.setActive(false);
        TermsDTO term3 = new TermsDTO();
        term3.setActive(true);

        List<TermsDTO> terms = Arrays.asList(
                term1, term2, term3
        );

        when(carRentalRetroFitClient.getLocationTerms(1L)).thenReturn(Observable.just(terms));

        List<TermsDTO> activeTerms = checkInService.getLocationTerms().blockingFirst();

        assertEquals(2, activeTerms.size());
    }

    @Test
    public void givenReservationDetails_whenPickupTimeBeforeAllowedRange_thenThrowException() {
        LocalDateTime now = LocalDateTime.now();

        ReservationDTO existingReservation = new ReservationDTO();
        existingReservation.setPickupTime("12:00");
        existingReservation.setPickupDate(now.toLocalDate());

        when(session.getReservation()).thenReturn(existingReservation);

        ReservationDTO updatedReservation = new ReservationDTO();
        updatedReservation.setPickupTime("07:00");

        Assertions.assertThrows(IllegalArgumentException.class, () -> checkInService.reservationDetails(updatedReservation).blockingFirst());
    }

    @Test
    public void givenReservationDetails_whenPickupTimeAfterAllowedRange_thenThrowException() {
        LocalDateTime now = LocalDateTime.now();

        ReservationDTO existingReservation = new ReservationDTO();
        existingReservation.setPickupTime("12:00");
        existingReservation.setPickupDate(now.toLocalDate());

        when(session.getReservation()).thenReturn(existingReservation);

        ReservationDTO updatedReservation = new ReservationDTO();
        updatedReservation.setPickupTime("20:00");

        Assertions.assertThrows(IllegalArgumentException.class, () -> checkInService.reservationDetails(updatedReservation).blockingFirst());
    }

    @Test
    public void givenUpdatedReservation_whenPickupTimeIsInThePast_thenThrowException() {
        LocalDateTime now = LocalDateTime.now();

        ReservationDTO existingReservation = new ReservationDTO();
        existingReservation.setPickupTime("12:00");
        existingReservation.setPickupDate(now.toLocalDate());

        when(session.getReservation()).thenReturn(existingReservation);

        ReservationDTO updatedReservation = new ReservationDTO();
        updatedReservation.setPickupTime("08:00");
        updatedReservation.setPickupDate(now.minusDays(1).toLocalDate());

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> checkInService.reservationDetails(updatedReservation).blockingFirst());
    }

    @Test
    public void givenProfile_whenCreateProfile_thenSaveInSessionAndRedirect() {
        ProfileDTO profile = new ProfileDTO();
        profile.setFirstName("Brenda");
        profile.setLastName("Brizola");

        when(carRentalRetroFitClient.submitPersonalInformation(profile))
                .thenReturn(Observable.just(profile));

        when(session.getReservation()).thenReturn(reservation);

        RedirectResponse response = checkInService.createProfile(profile).blockingFirst();

        assertEquals(RESERVATION_DETAILS, response.getGoTo());
        verify(session).setProfile(profile);
        verify(session.getReservation()).setProfile(profile);
    }

    @Test
    public void whenUpdateDriverDetails_thenProfileAndReservationAreUpdated() {
        ProfileDTO updatedProfile = new ProfileDTO();
        ProfileDTO existingProfile = new ProfileDTO();
        existingProfile.setLoyaltyNumber("12345");

        when(session.getProfile()).thenReturn(existingProfile);
        when(session.getReservation()).thenReturn(reservation);
        when(carRentalRetroFitClient.editProfile("12345", updatedProfile)).thenReturn(Observable.just(updatedProfile));
        when(carRentalRetroFitClient.updateReservation(any(), any(), any(), any())).thenReturn(Observable.just(new ReservationDTO()));

        RedirectResponse response = checkInService.updateDriverDetails(updatedProfile).blockingFirst();

        assertEquals(RESERVATION_DETAILS, response.getGoTo());
        verify(reservation).setProfile(updatedProfile);
    }
}
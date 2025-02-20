package com.example.Check_In_API.controller;

import com.example.Check_In_API.dtos.ProfileDTO;
import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.ReservationDTO;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.dtos.TermsDTO;
import com.example.Check_In_API.enums.CheckInRedirectEnum;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
class CheckInControllerTest {

    @Mock
    private CheckInService checkInService;

    @InjectMocks
    private CheckInController checkInController;

    @Mock
    private Session session;

    @Test
    public void givenSearchParams_whenGetReservation_thenReturnReservation() {
        when(checkInService.getReservation(anyString(), anyString(), anyString())).thenReturn(Observable.just(new Session()));

        RedirectResponse response = checkInController.getReservation(anyString(), anyString(), anyString()).blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.START));
    }

    @Test
    public void givenReservation_whenRedirectToProfileSearch_thenRedirectToProfileSearch(){
        RedirectResponse response = checkInController.redirectToProfileSearch().blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.PROFILE_SEARCH));
    }

    @Test
    public void givenProfile_whenCreateProfile_thenReturnRedirectResponse() {
        ProfileDTO profile = new ProfileDTO();
        RedirectResponse mockResponse = new RedirectResponse(session, CheckInRedirectEnum.CREATE_PROFILE);

        when(checkInService.createProfile(profile)).thenReturn(Observable.just(mockResponse));

        RedirectResponse response = checkInController.createProfile(profile).blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.CREATE_PROFILE));
    }

    @Test
    public void givenReservation_whenReservationDetails_thenReturnRedirectResponse() {
        ReservationDTO reservation = new ReservationDTO();
        RedirectResponse mockResponse = new RedirectResponse(session, CheckInRedirectEnum.RESERVATION_DETAILS);

        when(checkInService.reservationDetails(reservation)).thenReturn(Observable.just(mockResponse));

        RedirectResponse response = checkInController.reservationDetails(reservation).blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.RESERVATION_DETAILS));
    }

    @Test
    public void givenVehicleTermsRequest_whenVehicleTerms_thenReturnActiveTerms() {
        List<TermsDTO> termsList = List.of(new TermsDTO(), new TermsDTO());

        when(checkInService.getVehicleTerms()).thenReturn(Observable.just(termsList));

        List<TermsDTO> response = checkInController.vehicleTerms().blockingFirst();

        assertThat(response.size(), is(2));
    }

    @Test
    public void givenLocationTermsRequest_whenLocationTerms_thenReturnActiveTerms() {
        List<TermsDTO> termsList = List.of(new TermsDTO(), new TermsDTO(), new TermsDTO());

        when(checkInService.getLocationTerms()).thenReturn(Observable.just(termsList));

        List<TermsDTO> response = checkInController.locationTerms().blockingFirst();

        assertThat(response.size(), is(3));
    }

    @Test
    public void givenRedirectRequest_whenRedirectConfirmation_thenReturnRedirectResponse() {
        RedirectResponse response = checkInController.redirectToConfirmation().blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.CONFIRMATION));
    }
}
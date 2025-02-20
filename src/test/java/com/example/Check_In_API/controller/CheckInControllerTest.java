package com.example.Check_In_API.controller;

import com.example.Check_In_API.dtos.RedirectResponse;
import com.example.Check_In_API.dtos.Session;
import com.example.Check_In_API.enums.CheckInRedirectEnum;
import com.example.Check_In_API.service.CheckInService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        //when(checkInService.getSession()).thenReturn(new Session());
        
        RedirectResponse response = checkInController.redirectToProfileSearch().blockingFirst();

        assertThat(response.getGoTo(), is(CheckInRedirectEnum.PROFILE_SEARCH));
    }
}
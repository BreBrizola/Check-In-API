package com.example.Check_In_API.exception;

public class ReservationNotEligibleForCheckInException extends RuntimeException {
    public ReservationNotEligibleForCheckInException(String message) {
        super(message);
    }
}

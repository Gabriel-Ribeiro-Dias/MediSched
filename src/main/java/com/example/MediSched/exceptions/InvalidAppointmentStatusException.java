package com.example.MediSched.exceptions;

public class InvalidAppointmentStatusException extends RuntimeException {
    public InvalidAppointmentStatusException() {
        super("Invalid appointment status");
    }
    public InvalidAppointmentStatusException(String message) {
        super(message);
    }

}

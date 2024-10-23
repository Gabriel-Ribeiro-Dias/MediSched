package com.example.MediSched.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException() {super("Could not find appointment");}
    public AppointmentNotFoundException(String message) {super(message);}
}

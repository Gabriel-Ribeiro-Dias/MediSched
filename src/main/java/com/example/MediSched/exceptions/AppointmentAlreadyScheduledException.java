package com.example.MediSched.exceptions;

public class AppointmentAlreadyScheduledException extends RuntimeException {
    public AppointmentAlreadyScheduledException(){
        super("Appointment already scheduled");
    }
    public AppointmentAlreadyScheduledException(String message){super(message);}
}

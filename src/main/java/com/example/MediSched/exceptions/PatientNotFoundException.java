package com.example.MediSched.exceptions;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String message) {super(message);}
    public PatientNotFoundException() {super("Patient not found");}
}

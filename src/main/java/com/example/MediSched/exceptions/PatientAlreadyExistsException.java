package com.example.MediSched.exceptions;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
}

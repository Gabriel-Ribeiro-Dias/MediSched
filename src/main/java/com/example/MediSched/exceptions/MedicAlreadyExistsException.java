package com.example.MediSched.exceptions;

public class MedicAlreadyExistsException extends RuntimeException {
    public MedicAlreadyExistsException() {
        super("Medic already exists");
    }
    public MedicAlreadyExistsException(String message) {
        super(message);
    }
}

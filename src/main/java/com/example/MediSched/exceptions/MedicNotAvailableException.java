package com.example.MediSched.exceptions;

public class MedicNotAvailableException extends RuntimeException {
    public MedicNotAvailableException() {super("Medic not avaliable");}
    public MedicNotAvailableException(String message) {super(message);}
}

package com.example.MediSched.exceptions;

public class MedicNotFoundException extends RuntimeException {
    public MedicNotFoundException() {super("Could not find medic");}
    public MedicNotFoundException(String message) {super(message);}
}

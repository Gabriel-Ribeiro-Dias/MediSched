package com.example.MediSched.exceptions;

public class MedicScheduleNotDefinedException extends RuntimeException {
    public MedicScheduleNotDefinedException() {super("Medic schedule not defined");}
    public MedicScheduleNotDefinedException(String message) {super(message);}
}

package com.example.MediSched.exceptions;

public class UserAlredyExistsException extends RuntimeException {
    public UserAlredyExistsException(String message) {
        super(message);
    }
    public UserAlredyExistsException (){
        super("User already exists");
    }
}

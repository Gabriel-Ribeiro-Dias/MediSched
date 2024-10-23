package com.example.MediSched.infra;

import com.example.MediSched.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MedicNotFoundException.class)
    private ResponseEntity<RestErrorMessage> medicNotFound(MedicNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }
    @ExceptionHandler(PatientNotFoundException.class)
    private ResponseEntity<RestErrorMessage> patientNotFound(PatientNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }
    @ExceptionHandler(MedicNotAvailableException.class)
    private ResponseEntity<RestErrorMessage> medicNotAvaliable(MedicNotAvailableException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }
    @ExceptionHandler(AppointmentAlreadyScheduledException.class)
    private ResponseEntity<RestErrorMessage> appointmentAlreadyScheduled(AppointmentAlreadyScheduledException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage()));
    }
    @ExceptionHandler(AppointmentNotFoundException.class)
    private ResponseEntity<RestErrorMessage> appointmentNotFound(AppointmentNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage()));
    }
    @ExceptionHandler(MedicScheduleNotDefinedException.class)
    private ResponseEntity<RestErrorMessage> medicScheduleNotDefined(MedicScheduleNotDefinedException exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
    @ExceptionHandler(InvalidAppointmentStatusException.class)
    private ResponseEntity<RestErrorMessage> invalidAppointmentStatus(InvalidAppointmentStatusException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
    @ExceptionHandler(MedicAlreadyExistsException.class)
    private ResponseEntity<RestErrorMessage> medicAlreadyExists(MedicAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<RestErrorMessage> illegalArgument(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }
    @ExceptionHandler(UserAlredyExistsException.class)
    private ResponseEntity<RestErrorMessage> userAlreadyExists(UserAlredyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage()));
    }

}

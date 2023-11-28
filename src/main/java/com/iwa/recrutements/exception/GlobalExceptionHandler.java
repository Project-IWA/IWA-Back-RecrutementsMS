package com.iwa.recrutements.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Gère toutes les autres exceptions non spécifiées
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Gère une exception spécifique : ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Gère une exception spécifique : TypeEmploiNotFoundException
    @ExceptionHandler(TypeEmploiNotFoundException.class)
    public ResponseEntity<Object> handleTypeEmploiNotFoundException(TypeEmploiNotFoundException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Gère une exception spécifique : TypeEmploiNotFoundException
    @ExceptionHandler(EmploiNotFoundException.class)
    public ResponseEntity<Object> handleEmploiNotFoundException(EmploiNotFoundException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Gère une exception spécifique : MatchingServiceException
    @ExceptionHandler(MatchingServiceException.class)
    public ResponseEntity<Object> handleMatchingServiceException(MatchingServiceException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Gère une exception spécifique : DateDebutAfterDateFinException
    @ExceptionHandler(DateDebutAfterDateFinException.class)
    public ResponseEntity<Object> handleDateDebutAfterDateFinException(DateDebutAfterDateFinException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    //Gère une exception spécifique : CandidatNotFoundException
    @ExceptionHandler(CandidatNotFoundException.class)
    public ResponseEntity<Object> handleCandidatNotFoundException(CandidatNotFoundException ex) {
        System.out.println("Exception: " + ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // You can add more handlers for other exceptions if needed
}

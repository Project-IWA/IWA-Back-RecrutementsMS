package com.iwa.recrutements.exception;

public class CandidatNotFoundException extends RuntimeException{

    public CandidatNotFoundException(String email) {
        super("Candidat with email + " + email + "not found");
    }
}

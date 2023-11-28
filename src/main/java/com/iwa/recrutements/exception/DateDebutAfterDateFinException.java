package com.iwa.recrutements.exception;

import java.util.Date;

public class DateDebutAfterDateFinException extends RuntimeException {
    public DateDebutAfterDateFinException(String message) {
        super(message);
    }

    public DateDebutAfterDateFinException(Date dateDebut, Date dateFin) {
        super(dateDebut + " is after " + dateFin);
    }

}

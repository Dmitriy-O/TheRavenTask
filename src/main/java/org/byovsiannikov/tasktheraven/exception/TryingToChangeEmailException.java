package org.byovsiannikov.tasktheraven.exception;

public class TryingToChangeEmailException extends RuntimeException {

    public TryingToChangeEmailException (String errorMessage) {
        super(errorMessage);
    }
}

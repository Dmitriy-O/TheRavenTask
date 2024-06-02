package org.byovsiannikov.tasktheraven.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandling {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions (MethodArgumentNotValidException exception) {
        Map<String, String> handleInvalidMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> handleInvalidMap.put(error.getField(), error.getDefaultMessage()));
        return handleInvalidMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFoundException () {
        return "Customer not found";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public String handleEntityExistsException () {
        return "Customer already exists";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TryingToChangeEmailException.class)
    public String handleTryingToChangeEmailException () {
        return "You could not change email";
    }

}

package ru.sakhapov.e_commerce_api.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalStateException extends RuntimeException{
    public IllegalStateException(String message) {
        super(message);
    }
}

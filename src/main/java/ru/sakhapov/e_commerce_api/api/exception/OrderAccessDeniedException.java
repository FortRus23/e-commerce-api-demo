package ru.sakhapov.e_commerce_api.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrderAccessDeniedException extends RuntimeException {
    public OrderAccessDeniedException() {
        super("Access denied to the requested order");
    }
}
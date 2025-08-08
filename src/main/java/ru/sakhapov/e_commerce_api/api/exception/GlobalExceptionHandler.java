package ru.sakhapov.e_commerce_api.api.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sakhapov.e_commerce_api.api.dto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationError(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        ErrorDto errorDto = ErrorDto.builder()
                .error("validation_error")
                .errorDescription(message)
                .build();

        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Bad request");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDto> handleForbidenException(InvalidCredentialsException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Unauthorized");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Not found");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
}
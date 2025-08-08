package ru.sakhapov.e_commerce_api.api.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sakhapov.e_commerce_api.api.dto.ErrorDto;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        ErrorDto errorDto = ErrorDto.builder()
                .error("validation_error")
                .errorDescription("Validation failed")
                .validationErrors(errors)
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

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("Username already exist");
        errorDto.setErrorDescription(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
}
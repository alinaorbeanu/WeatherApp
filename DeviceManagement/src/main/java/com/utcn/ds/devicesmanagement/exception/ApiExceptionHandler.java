package com.utcn.ds.devicesmanagement.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundObjectException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(NotFoundObjectException objectNotFoundException) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, objectNotFoundException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class, PSQLException.class})
    protected ResponseEntity<ApiError> handleBadRequestException(RuntimeException exception) {
        return getResponseEntity(exception);
    }

    @Override
    @ExceptionHandler({AlreadyExistsException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders httpHeaders, @NonNull HttpStatusCode httpStatusCode, @NonNull WebRequest webRequest) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        StringBuilder stringBuilder = new StringBuilder();
        exception.getBindingResult().getAllErrors()
                .stream()
                .filter(FieldError.class::isInstance)
                .map(FieldError.class::cast).forEach(error -> insertErrorMessages(apiError, error, stringBuilder));
        apiError.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private void insertErrorMessages(ApiError apiError, FieldError errorField, StringBuilder errors) {
        String errorMessage = errorField.getDefaultMessage();
        errors.append(errorMessage).append(" ");
        apiError.setMessage(errors.toString());
    }

    private ResponseEntity<ApiError> getResponseEntity(Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(exception.getMessage());
        apiError.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
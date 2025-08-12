package com.chokhaniyash.streambridge.Exceptions.handler;

import com.chokhaniyash.streambridge.Exceptions.AppException;
import com.chokhaniyash.streambridge.dto.response.ErrorResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AppException.class})
    public ResponseEntity<ErrorResponse> appExceptionHandler(AppException exception){
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .type(exception.getType())
                .build();
        return new ResponseEntity<>(error, exception.getStatus());
    }

    @ExceptionHandler({MethodValidationException.class, ValidationException.class})
    public ResponseEntity<ErrorResponse> validationExceptionHandler(Exception exception){
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .type("INVALID_REQUEST")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception){
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(exception.getMessage())
                .type("INTERNAL_SERVER_ERROR")
                .build();
        exception.printStackTrace(); // Prints the full stack trace to System.err
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

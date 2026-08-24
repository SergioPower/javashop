package com.javashop.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javashop.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidarion(
            MethodArgumentNotValidException ex) {

        var errors = ex.getBindingResult().getFieldErrors();

        errors.forEach(error -> System.out.println(
                error.getField() + " -> " + error.getDefaultMessage()));

        String message = errors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        errors.forEach(error -> System.out.println(
                error.getField() + " -> " + error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                400,
                message);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
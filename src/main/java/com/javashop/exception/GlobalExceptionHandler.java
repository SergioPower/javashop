package com.javashop.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

import com.javashop.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
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

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductoNotFound(ProductoNotFoundException ex){
        String message = ex.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                404,
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);   
    }
}
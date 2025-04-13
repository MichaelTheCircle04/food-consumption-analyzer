package com.mtrifonov.food.consumption.analyzer.controller;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<Map<String, String>> handleRuntimeException(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest().body(Map.of("exception", e.getMessage()));
    }
}

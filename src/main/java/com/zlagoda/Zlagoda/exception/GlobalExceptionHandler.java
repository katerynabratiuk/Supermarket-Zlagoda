package com.zlagoda.Zlagoda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NotEnoughProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotEnoughProductException(NotEnoughProductException ex) {
        return Map.of("error", ex.getMessage());
    }

}

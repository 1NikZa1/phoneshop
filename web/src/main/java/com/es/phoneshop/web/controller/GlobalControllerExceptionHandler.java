package com.es.phoneshop.web.controller;

import com.es.core.exception.NoSuchPageFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 409
    @ExceptionHandler(NoSuchPageFoundException.class)
    public String handleConflict() {
        return "/errorPages/productNotFound";
    }

}

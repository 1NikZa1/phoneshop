package com.es.phoneshop.web.controller;

import com.es.core.exception.NoSuchOrderException;
import com.es.core.exception.NoSuchPageFoundException;
import com.es.core.exception.PhoneNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchPageFoundException.class, PhoneNotFoundException.class, NoSuchOrderException.class})
    public String handleConflict() {
        return "/errorPages/productNotFound";
    }

}

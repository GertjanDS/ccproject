package com.gertjan.ccproject.controller;

import com.gertjan.ccproject.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e, WebRequest request) {
        // This is technically a BAD way to do this in a real life application, as is technically allows ID enumeration
        // but for that reason we use UUID's, so it is fine...
        return handleExceptionInternal(e,
                "No entity with given ID found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}

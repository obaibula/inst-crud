package com.example.instcrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> userNotFoundHandler(UserNotFoundException e){
        var response = new UserErrorResponse();

        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserPropertyValueException.class)
    public ResponseEntity<UserErrorResponse>
    userPropertyValueHandler(UserPropertyValueException e){
        var response = new UserErrorResponse();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

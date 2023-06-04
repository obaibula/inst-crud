package com.example.instcrud.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
@Log4j2
public class UserExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> userNotFoundHandler(UserNotFoundException e){
        var response = new UserErrorResponse();
        String message = e.getMessage();

        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(message);
        response.setTimeStamp(System.currentTimeMillis());

        log.error(message);
        e.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserPropertyValueException.class)
    public ResponseEntity<UserErrorResponse>
    userPropertyValueHandler(UserPropertyValueException e){
        var response = new UserErrorResponse();
        String message = e.getMessage();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(message);
        response.setTimeStamp(System.currentTimeMillis());

        log.error(message);
        e.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle unique issued exception for example
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserErrorResponse>
    dataIntegrityViolationHandler(DataIntegrityViolationException e){
        var response = new UserErrorResponse();
        String message = e.getMessage();

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(message);
        response.setTimeStamp(System.currentTimeMillis());

        log.error(message);
        e.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

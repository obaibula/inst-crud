package com.example.instcrud.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class PostExceptionAdvice {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<PostErrorResponse> postNotFoundHandler(PostNotFoundException e) {
        String message = e.getMessage();
        var response = new PostErrorResponse(HttpStatus.NOT_FOUND.value(),
                message,
                System.currentTimeMillis());

        log.error(message);
        e.printStackTrace();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

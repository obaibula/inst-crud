package com.example.instcrud.exception;

public record UserErrorResponse(int status,
                                String message,
                                long timeStamp) {
}

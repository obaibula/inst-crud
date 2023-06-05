package com.example.instcrud.exception;

public record PostErrorResponse(int status,
                                String message,
                                long timeStamp) {
}

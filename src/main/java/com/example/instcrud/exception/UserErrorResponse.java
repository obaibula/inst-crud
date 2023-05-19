package com.example.instcrud.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserErrorResponse {
    private int status;
    private String message;
    private long timeStamp;
}

package com.example.instcrud.dto.post;

public record PostRequestDTO(String url,
                             String caption,
                             Float lat,
                             Float lng,
                             Long userId) {
}

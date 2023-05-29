package com.example.instcrud.dto;

import java.time.LocalDateTime;

public record PostDTO(Long id,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String url,
                      String caption,
                      Float lat,
                      Float lng,
                      Long userId) {
}

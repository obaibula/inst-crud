package com.example.instcrud.dto.post;

import java.time.LocalDateTime;

public record PostResponseDTO(Long id,
                              String url,
                              String caption,
                              Float lat,
                              Float lng,
                              Long userId) {
}

package com.web.BlogApp.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CommentDTO(UUID id, @NotBlank String author, @NotBlank String content) {
}

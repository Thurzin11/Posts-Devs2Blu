package com.web.BlogApp.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PostRecordDto(UUID id, @NotBlank String autor, @NotBlank String titulo, @NotBlank String texto) {

}

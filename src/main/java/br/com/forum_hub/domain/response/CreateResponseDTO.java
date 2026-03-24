package br.com.forum_hub.domain.response;

import jakarta.validation.constraints.NotBlank;

public record CreateResponseDTO(
        @NotBlank String message,
        @NotBlank String author) {
}

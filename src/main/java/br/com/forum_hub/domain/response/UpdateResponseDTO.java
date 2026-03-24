package br.com.forum_hub.domain.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateResponseDTO(
        @NotNull Long id,
        @NotBlank String message
) {
}

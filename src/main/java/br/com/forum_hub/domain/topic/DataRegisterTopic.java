package br.com.forum_hub.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataRegisterTopic(
         @NotBlank String title,
         @NotBlank String message,
         @NotBlank String author,
         @NotNull Long courseId
) {
}

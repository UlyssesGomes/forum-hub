package br.com.forum_hub.domain.topic;

import jakarta.validation.constraints.NotNull;

public record DataActualizationTopic(
        @NotNull Long id,
        String title,
        String message,
        Long courseId
) {
}

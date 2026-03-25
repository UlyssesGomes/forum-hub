package br.com.forum_hub.domain.response;

import java.time.LocalDateTime;

public record ListResponseDTO(
        Long id,
        String message,
        String author,
        LocalDateTime dataCriacao,
        Boolean isSolved
) {
    public ListResponseDTO(Response response) {
        this(response.getId(), response.getMessage(), response.getAuthor().getFullName(), response.getCreationDate(), response.getIsSolved());
    }
}

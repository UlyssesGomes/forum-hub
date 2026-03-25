package br.com.forum_hub.domain.topic;

import java.time.LocalDateTime;

public record DataListTopic(
        Long id,
        String title,
        String message,
        String author,
        Status status,
        LocalDateTime creationDate,
        Integer quantityResponses,
        String course
) {
    public DataListTopic(Topic topic) {
        this(topic.getId(), topic.getTitle(), topic.getMessage(), topic.getAuthor().getFullName(), topic.getStatus(), topic.getCreationDate(), topic.getQuantityResponses(), topic.getCourse().getName());
    }
}

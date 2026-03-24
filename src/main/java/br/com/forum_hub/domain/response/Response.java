package br.com.forum_hub.domain.response;

import br.com.forum_hub.domain.topic.Topic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String author;
    private LocalDateTime creationDate;
    private Boolean isSolved;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public Response(CreateResponseDTO dataDTO, Topic topic) {
        this.message = dataDTO.message();
        this.author = dataDTO.author();
        this.creationDate = LocalDateTime.now();
        this.isSolved = false;
        this.topic = topic;
    }

    public Response updateInformations(UpdateResponseDTO dados) {
        this.message = dados.message();
        return this;
    }

    public Response checkAsSolved() {
        this.isSolved = true;
        return this;
    }
}

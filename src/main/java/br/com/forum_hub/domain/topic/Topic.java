package br.com.forum_hub.domain.topic;

import br.com.forum_hub.domain.course.Category;
import br.com.forum_hub.domain.course.Course;
import br.com.forum_hub.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "topics")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    @ManyToOne
    @JoinColumn(name="author_id")
    private User author;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean isOpen;
    private Integer quantityResponses;
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Topic(DataRegisterTopic dataRegisterTopic, User author, Course course) {
        this.title = dataRegisterTopic.title();
        this.message = dataRegisterTopic.message();
        this.author = author;
        this.creationDate = LocalDateTime.now();
        this.status = Status.UNANSWERED;
        this.isOpen = true;
        this.quantityResponses = 0;
        this.category = course.getCategory();
        this.course = course;
    }

    public Topic updateInformation(DataActualizationTopic data, Course course) {
        if(data.title() != null){
            this.title = data.title();
        }
        if(data.message() != null){
            this.message = data.message();
        }
        if(this.course != null){
            this.course = course;
        }
        return this;
    }

    public void incrementResponses() {
        this.quantityResponses++;
    }

    public void decrementResponses() {
        this.quantityResponses--;
    }

    public void close() {
        this.isOpen = false;
    }
}
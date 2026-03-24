package br.com.forum_hub.domain.topic;

import org.springframework.data.jpa.domain.Specification;

public class TopicSpecification {

    public static Specification<Topic> hasCategory(String category) {
        return (root, query, builder) -> category == null ? null : builder.equal(root.get("category"), category);
    }

    public static Specification<Topic> hasCourseId(Long idCourse) {
        return (root, query, builder) -> idCourse == null ? null : builder.equal(root.get("course").get("id"), idCourse);
    }

    public static Specification<Topic> isOpen() {
        return (root, query, builder) -> builder.isTrue(root.get("isOpen"));
    }

    public static Specification<Topic> unanswered(Boolean unanswered) {
        return (root, query, builder) -> (unanswered == null || !unanswered) ? null : builder.equal(root.get("status"), Status.UNANSWERED);
    }

    public static Specification<Topic> solved(Boolean solveds) {
        return (root, query, builder) -> (solveds == null || !solveds) ? null : builder.equal(root.get("status"), Status.SOLVED);
    }
}


package br.com.forum_hub.domain.course;

public record CourseDTO(
        Long id,
        String nome,
        Category category) {
    public CourseDTO(Course course) {
        this(course.getId(), course.getName(), course.getCategory());
    }
}
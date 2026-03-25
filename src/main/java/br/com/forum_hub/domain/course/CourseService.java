package br.com.forum_hub.domain.course;

import br.com.forum_hub.infra.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course save(CreateCourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.name());
        course.setCategory(courseDTO.category());

        return repository.save(course);
    }

    public Course searchById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException("Curso não encontrado!"));
    }

    public Page<CourseDTO> list(Category category, Pageable page) {
        if(category != null)
            return repository.findByCategory(category, page).map(CourseDTO::new);
        return repository.findAll(page).map(CourseDTO::new);

    }
}

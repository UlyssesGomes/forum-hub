package br.com.forum_hub.domain.course;

import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public Course searchById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RegraDeNegocioException("Curso não encontrado!"));
    }

    public Page<CourseDTO> list(Category category, Pageable paginacao) {
        if(category != null)
            return repository.findByCategory(category, paginacao).map(CourseDTO::new);
        return repository.findAll(paginacao).map(CourseDTO::new);

    }
}

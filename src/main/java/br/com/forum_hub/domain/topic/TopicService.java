package br.com.forum_hub.domain.topic;

import br.com.forum_hub.domain.course.CourseService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    private final TopicRepository repository;
    private final CourseService courseService;

    public TopicService(TopicRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    @Transactional
    public Topic create(DataRegisterTopic data) {
        var curso = courseService.searchById(data.courseId());
        var topic = new Topic(data, curso);
        return repository.save(topic);
    }

    public Page<DataListTopic> list(String category, Long idCourse, Boolean unanswered, Boolean solved, Pageable pagination) {
        Specification<Topic> spec = Specification.allOf(
                TopicSpecification.isOpen(),
                category    != null ? TopicSpecification.hasCategory(category)   : Specification.unrestricted(),
                idCourse    != null ? TopicSpecification.hasCourseId(idCourse)   : Specification.unrestricted(),
                unanswered  != null ? TopicSpecification.unanswered(unanswered)  : Specification.unrestricted(),
                solved      != null ? TopicSpecification.solved(solved)          : Specification.unrestricted()
        );

        Page<Topic> topics = repository.findAll(spec, pagination);
        return topics.map(DataListTopic::new);
    }

    @Transactional
    public Topic update(DataActualizationTopic dados) {
        var topic = searchById(dados.id());
        var curso = courseService.searchById(dados.courseId());
        return topic.updateInformation(dados, curso);
    }

    @Transactional
    public void delete(Long id) {
        var topic = searchById(id);
        if (topic.getStatus() == Status.UNANSWERED)
            repository.deleteById(id);
        else
            throw new RegraDeNegocioException("Você não pode apagar um tópico que já foi respondido.");
    }

    public Topic searchById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Tópico não encontrado!"));

    }

    @Transactional
    public void close(Long id) {
        var topic = searchById(id);
        topic.close();
    }
}

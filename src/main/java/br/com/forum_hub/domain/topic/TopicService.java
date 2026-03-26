package br.com.forum_hub.domain.topic;

import br.com.forum_hub.domain.course.CourseService;
import br.com.forum_hub.domain.hierarchy.HierarchyService;
import br.com.forum_hub.domain.role.RoleEnum;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.infra.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TopicService {

    @Autowired
    private TopicRepository repository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private HierarchyService hierarchyService;

    @Transactional
    public Topic create(DataRegisterTopic data) {
        var course = courseService.searchById(data.courseId());
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var topic = new Topic(data, author, course);
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var topic = searchById(id);

        if(hierarchyService.isntSameUserAndHaventPermission(user, topic.getAuthor(), "ROLE_" + RoleEnum.MODERATOR.name()))
            throw new BusinessException("User doesn't have permission or this is not their own topic.");

        if (topic.getStatus() == Status.UNANSWERED)
            repository.deleteById(id);
        else
            throw new BusinessException("Você não pode apagar um tópico que já foi respondido.");
    }

    public Topic searchById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Tópico não encontrado!"));

    }

    @Transactional
    public void close(Long id) {
        var topic = searchById(id);
        topic.close();
    }
}

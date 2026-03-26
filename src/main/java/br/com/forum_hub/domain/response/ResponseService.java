package br.com.forum_hub.domain.response;

import br.com.forum_hub.domain.hierarchy.HierarchyService;
import br.com.forum_hub.domain.role.RoleEnum;
import br.com.forum_hub.domain.topic.Status;
import br.com.forum_hub.domain.topic.TopicService;
import br.com.forum_hub.domain.user.User;
import br.com.forum_hub.infra.exception.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    private final ResponseRepository repository;
    private final TopicService topicService;

    @Autowired
    private HierarchyService hierarchyService;

    public ResponseService(ResponseRepository repository, TopicService topicService) {
        this.repository = repository;
        this.topicService = topicService;
    }

    @Transactional
    public Response create(CreateResponseDTO dataDTO, Long idTopic) {
        var dbTopic = topicService.searchById(idTopic);

        if(!dbTopic.getIsOpen()) {
            throw new BusinessException("O tópico está fechado! Você não pode adicionar mais respostas.");
        }

        if(dbTopic.getQuantityResponses() == 0) {
            dbTopic.setStatus(Status.ANSWERED);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        dbTopic.incrementResponses();

        var response = new Response(dataDTO, user, dbTopic);
        return repository.save(response);
    }

    @Transactional
    public Response update(UpdateResponseDTO dataDTO) {
        var response = searchById(dataDTO.id());
        return response.updateInformations(dataDTO);
    }

    public List<Response> searchResponseTopic(Long id){
        return repository.findByTopicId(id);
    }

    @Transactional
    public Response checkAsSolved(Long id) throws AccessDeniedException {
        var response = searchById(id);
        var topic = response.getTopic();

        var userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!topic.getAuthor().getId().equals(userLogged.getId()))
            if(hierarchyService.userHaventPermission(userLogged, "ROLE_"+ RoleEnum.MODERATOR.name()))
                throw new AccessDeniedException("You cannot check this answer as solution!");

        if(topic.getStatus() == Status.SOLVED)
            throw new BusinessException("The topic was solved.");

        topic.setStatus(Status.SOLVED);
        return response.checkAsSolved();
    }

    @Transactional
    public void delete(Long id) throws AccessDeniedException {
        var response = searchById(id);
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(hierarchyService.isntSameUserAndHaventPermission(user, response.getAuthor(), "ROLE_"+ RoleEnum.MODERATOR.name()))
            throw new AccessDeniedException("You cannot delete this answer!");

        var topic = response.getTopic();

        repository.deleteById(id);

        topic.decrementResponses();
        if (topic.getQuantityResponses() == 0)
            topic.setStatus(Status.UNANSWERED);
        else if(response.getIsSolved())
            topic.setStatus(Status.ANSWERED);
    }

    public Response searchById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Resposta não encontrada!"));
    }
}

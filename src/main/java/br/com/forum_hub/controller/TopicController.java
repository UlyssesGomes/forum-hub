package br.com.forum_hub.controller;

import br.com.forum_hub.domain.response.ResponseService;
import br.com.forum_hub.domain.topic.DataActualizationTopic;
import br.com.forum_hub.domain.topic.DataRegisterTopic;
import br.com.forum_hub.domain.topic.DataDetailsTopic;
import br.com.forum_hub.domain.topic.DataListTopic;
import br.com.forum_hub.domain.topic.TopicService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("topics")
public class TopicController {
    private final TopicService service;
    private final ResponseService responseService;

    public TopicController(TopicService service, ResponseService responseService) {
        this.service = service;
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<DataListTopic> cadastrar(@RequestBody @Valid DataRegisterTopic dados, UriComponentsBuilder uriBuilder){
        var topico = service.create(dados);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DataListTopic(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DataListTopic>> listar(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, name = "unanswered") Boolean unanswered,
            @RequestParam(required = false) Boolean isSolved,
            @PageableDefault(size = 10, sort = {"creationDate"}) Pageable page){

        var pagina = service.list(category, courseId, unanswered,
                isSolved, page);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataDetailsTopic> detalhar(@PathVariable Long id){
        var topico = service.searchById(id);
        var respostas = responseService.searchResponseTopic(id);
        return ResponseEntity.ok(new DataDetailsTopic(topico, respostas));
    }

    @PutMapping
    public ResponseEntity<DataListTopic> atualizar(@RequestBody @Valid DataActualizationTopic dados){
        var topico = service.update(dados);
        return ResponseEntity.ok(new DataListTopic(topico));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> fechar(@PathVariable Long id){
        service.close(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}

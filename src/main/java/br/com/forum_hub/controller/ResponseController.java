package br.com.forum_hub.controller;

import br.com.forum_hub.domain.response.ListResponseDTO;
import br.com.forum_hub.domain.response.CreateResponseDTO;
import br.com.forum_hub.domain.response.UpdateResponseDTO;
import br.com.forum_hub.domain.response.ResponseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("topics/{idTopic}/responses")
public class ResponseController {

    private final ResponseService service;

    public ResponseController(ResponseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ListResponseDTO> create(@PathVariable Long idTopic, @RequestBody @Valid CreateResponseDTO dataDTO, UriComponentsBuilder uriBuilder){
        var response = service.create(dataDTO, idTopic);
        var uri = uriBuilder.path("topics/{idTopic}/responses/{id}").buildAndExpand(response.getTopic().getId(), response.getId()).toUri();
        return ResponseEntity.created(uri).body(new ListResponseDTO(response));
    }

    @PutMapping
    public ResponseEntity<ListResponseDTO> update(@RequestBody @Valid UpdateResponseDTO dataDTO){
        var response = service.update(dataDTO);
        return ResponseEntity.ok(new ListResponseDTO(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListResponseDTO> checkAsSolved(@PathVariable Long id) throws AccessDeniedException {
        var response = service.checkAsSolved(id);
        return ResponseEntity.ok(new ListResponseDTO(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws AccessDeniedException {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

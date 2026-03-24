package br.com.forum_hub.domain.topic;

import br.com.forum_hub.domain.response.ListResponseDTO;
import br.com.forum_hub.domain.response.Response;

import java.util.List;

public record DataDetailsTopic(DataListTopic dadosListagem, List<ListResponseDTO> respostas) {
    public DataDetailsTopic(Topic topic, List<Response> responses) {
        this(new DataListTopic(topic), responses.stream().map(ListResponseDTO::new).toList());
    }
}
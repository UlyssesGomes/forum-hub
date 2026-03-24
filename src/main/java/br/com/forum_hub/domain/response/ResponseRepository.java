package br.com.forum_hub.domain.response;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByTopicId(Long id);
}

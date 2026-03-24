package br.com.forum_hub.controller;

import br.com.forum_hub.domain.course.Category;
import br.com.forum_hub.domain.course.CourseDTO;
import br.com.forum_hub.domain.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> list(@RequestParam(required = false) Category category,
                                                @PageableDefault(size = 10, sort = {"name"}) Pageable page){
        var pagina = service.list(category, page);
        return ResponseEntity.ok(pagina);
    }

}

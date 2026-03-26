package br.com.forum_hub.controller;

import br.com.forum_hub.domain.course.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody @Valid CreateCourseDTO courseDTO, UriComponentsBuilder uriBuilder) {
        var course = service.save(courseDTO);
        CourseDTO responseCourse = new CourseDTO(course);
        var uri = uriBuilder.path("/course/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(uri).body(responseCourse);
    }

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> list(@RequestParam(required = false) Category category,
                                                @PageableDefault(size = 10, sort = {"name"}) Pageable page){
        var pagina = service.list(category, page);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDTO> getOne(@PathVariable Long id) {
        var course = service.searchById(id);
        CourseDTO courseDTO = new CourseDTO(course);
        return ResponseEntity.ok(courseDTO);
    }

}

package vsh.students.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.service.SelectsService;

import java.util.List;

@RestController
@RequestMapping("/selects")
public class SelectsController {
    private final SelectsService selectsService;

    public SelectsController(SelectsService selectsService) {
        this.selectsService = selectsService;
    }

    @GetMapping(value = "/presents/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentAbsenceCountDTO>> getAbsencesByCourse(@PathVariable long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(selectsService.getPresentsByCourse(course_id));
    }

    @GetMapping(value = "/course/average/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageGradeByCourse(@PathVariable long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(selectsService.getAverageGradeByCourse(course_id));
    }
}

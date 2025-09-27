package vsh.students.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.GradeDTO;
import vsh.students.model.Grade;
import vsh.students.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradesController {
    @Autowired
    private GradeService gradeService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Grade> addGrade(@RequestBody GradeDTO gradeDTO) {
        Grade saved = gradeService.addGrade(gradeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/student/{student_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Grade>> getGradeByStudent(@PathVariable long student_id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeService.getGradeByStudentId(student_id));
    }

    @GetMapping(value = "/course/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Grade>> getGradeByCourse(@PathVariable long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeService.getGradeByCourseId(course_id));
    }
}

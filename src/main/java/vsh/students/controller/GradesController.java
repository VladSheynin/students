package vsh.students.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.GradeDTO;
import vsh.students.model.Grade;
import vsh.students.service.GradeService;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradesController {
    private final GradeService gradeService;

    public GradesController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<Grade> addGrade(@RequestBody GradeDTO gradeDTO) {
        Grade saved = gradeService.addGrade(gradeDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping(value = "/student/{student_id}")
    public ResponseEntity<List<Grade>> getGradeByStudent(@PathVariable long student_id) {
        return ResponseEntity.ok(gradeService.getGradeByStudentId(student_id));
    }

    @GetMapping(value = "/course/{course_id}")
    public ResponseEntity<List<Grade>> getGradeByCourse(@PathVariable long course_id) {
        return ResponseEntity.ok(gradeService.getGradeByCourseId(course_id));
    }
}

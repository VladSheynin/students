package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vsh.students.model.Grade;
import vsh.students.service.GradeService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GradesController {
    @Autowired
    private GradeService gradeService;

    @PostMapping(value = "/grade", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addGrade(@RequestParam long student_id,@RequestParam long course_id,@RequestParam int gradeSize,@RequestParam LocalDate gradeAt) {
        try {
            gradeService.addGrade(student_id, course_id, gradeSize, gradeAt);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(value = "/grade/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Grade>> getGradeByStudent(@RequestParam long student_id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeService.getGradeByStudentId(student_id));
    }

    @GetMapping(value = "/grade/course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Grade>> getGradeByCourse(@RequestParam long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(gradeService.getGradeByCourseId(course_id));
    }
}

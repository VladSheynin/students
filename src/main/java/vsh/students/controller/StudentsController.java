package vsh.students.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.StudentDTO;
import vsh.students.model.Student;
import vsh.students.service.StudentsService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentsController {
    private final StudentsService studentsService;

    public StudentsController(StudentsService studentsService) {
        this.studentsService = studentsService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable long id) {
        return ResponseEntity.ok().body(studentsService.getStudentById(id));
    }

    @GetMapping(value = "/name/{student_name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String student_name) {
        return ResponseEntity.ok().body(studentsService.getStudentByName(student_name));
    }


    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentsService.getAllStudents();
        if (students.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.ok().body(students);
    }

    @GetMapping(value = "/group/{group_name}")
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable String group_name) {
        List<Student> students = studentsService.getStudentsByGroup(group_name);
        if (students.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.ok().body(students);
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody StudentDTO studentDTO) {
        Student saved = studentsService.addStudent(studentDTO);
        return ResponseEntity.ok().body(saved);
    }

}

package vsh.students.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    StudentsService studentsService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> getStudentById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(studentsService.getStudentById(id));
    }

    @GetMapping(value = "/name/{student_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> getStudentByName(@PathVariable String student_name) {
        return ResponseEntity.status(HttpStatus.OK).body(studentsService.getStudentByName(student_name));
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentsService.getAllStudents();
        if (students.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @GetMapping(value = "/group/{group_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable String group_name) {
        List<Student> students = studentsService.getStudentsByGroup(group_name);
        if (students.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(students);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> addStudent(@RequestBody StudentDTO studentDTO) {
        Student saved = studentsService.addStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}

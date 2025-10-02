package vsh.students.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.TeacherDTO;
import vsh.students.model.Teacher;
import vsh.students.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeachersController {

    private final TeacherService teacherService;

    public TeachersController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherById(id));
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.ok().body(teachers);
    }

    @GetMapping(value = "/department/{department}")
    public ResponseEntity<List<Teacher>> getTeachersByGroup(@PathVariable String department) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(department);
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.ok().body(teachers);
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@RequestBody TeacherDTO teacherDTO) {
        Teacher saved = teacherService.addTeacher(teacherDTO);
        return ResponseEntity.ok().build();
    }
}
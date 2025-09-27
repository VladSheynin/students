package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    TeacherService teacherService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Teacher> getTeacherById(@PathVariable long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @GetMapping(value = "/department/{department}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getTeachersByGroup(@PathVariable String department) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(department);
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Teacher> addTeacher(@RequestBody TeacherDTO teacherDTO) {
        Teacher saved = teacherService.addTeacher(teacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vsh.students.model.Teacher;
import vsh.students.service.TeacherService;

import java.util.List;

@RestController
public class TeachersController {
    @Autowired
    TeacherService teacherService;

    @GetMapping(value = "/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Teacher> getTeacherById(@RequestParam long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(teacherService.getTeacherById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/teacher/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @GetMapping(value = "/teacher/department", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Teacher>> getTeachersByGroup(@RequestParam String department) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(department);
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @PostMapping(value = "/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTeacher(@RequestParam String name,@RequestParam String department) {
        try {
            teacherService.addTeacher(name, department);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonUniqueResultException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }
}
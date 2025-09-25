package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.CourseDTO;
import vsh.students.model.Course;
import vsh.students.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CourseService courseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCourse(@RequestBody CourseDTO courseDTO) {
        try {
            courseService.addCourse(courseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonUniqueResultException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseById(@PathVariable long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/name/{course_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseByName(@PathVariable String course_name) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseByName(course_name));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/teacher/name/{teacher_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherName(@PathVariable String teacher_name) {
        List<Course> courseList = courseService.getCoursesByTeacherName(teacher_name);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @GetMapping(value = "/teacher/id/{teacher_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherId(@PathVariable long teacher_id) {
        List<Course> courseList = courseService.getCoursesByTeacherId(teacher_id);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }
}

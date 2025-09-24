package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.model.Course;
import vsh.students.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CoursesController {
    @Autowired
    private CourseService courseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseById(@RequestParam long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping(value = "/teacherName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherName(@RequestParam String teacher_name) {
        List<Course> courseList = courseService.getCoursesByTeacherName(teacher_name);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @GetMapping(value = "/teacherId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherId(@RequestParam long teacher_id) {
        List<Course> courseList = courseService.getCoursesByTeacherId(teacher_id);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCourse(@RequestParam String course_name,@RequestParam long teacher_id,@RequestParam String students_ids) {
        try {
            courseService.addCourse(course_name, teacher_id, students_ids);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NonUniqueResultException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }
}

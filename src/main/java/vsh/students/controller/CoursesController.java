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
import vsh.students.model.Course;
import vsh.students.service.CourseHelper;

import java.util.List;

@RestController
public class CoursesController {
    @Autowired
    CourseHelper courseHelper;

    @GetMapping(value = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseById(@RequestParam long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(courseHelper.getCourseById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/course/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> teachers = courseHelper.getAllCourses();
        if (teachers.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(teachers);
    }

    @GetMapping(value = "/course/teacherName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherName(@RequestParam String teacher_name) {
        List<Course> courseList = courseHelper.getCoursesByTeacherName(teacher_name);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @GetMapping(value = "/course/teacherId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherId(@RequestParam long teacher_id) {
        List<Course> courseList = courseHelper.getCoursesByTeacherId(teacher_id);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @PostMapping(value = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTeacher(String course_name, long teacher_id, String students_ids) {
        try {
            courseHelper.addCourse(course_name, teacher_id, students_ids);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NonUniqueResultException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }
}

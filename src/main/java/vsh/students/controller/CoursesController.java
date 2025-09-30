package vsh.students.controller;

import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.CourseDTO;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.model.Course;
import vsh.students.repositories.CourseRepository;
import vsh.students.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CourseService courseService;

    public CoursesController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> addCourse(@RequestBody CourseDTO courseDTO) {
        Course saved = courseService.addCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseById(@PathVariable long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/name/{course_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> getCourseByName(@PathVariable String course_name) {
        Course course = courseService.getCourseByName(course_name);
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/teacher/name/{teacher_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherName(@PathVariable String teacher_name) {
        List<Course> courseList = courseService.getCoursesByTeacherName(teacher_name);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @GetMapping(value = "/teacher/id/{teacher_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Course>> getCourseByTeacherId(@PathVariable long teacher_id) {
        List<Course> courseList = courseService.getCoursesByTeacherId(teacher_id);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.status(HttpStatus.OK).body(courseList);
    }

    @GetMapping(value = "/presents/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentAbsenceCountDTO>> getAbsencesByCourse(@PathVariable long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getPresentsByCourse(course_id));
    }

    @GetMapping(value = "/average/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageGradeByCourse(@PathVariable long course_id) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getAverageGradeByCourse(course_id));
    }

}

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

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.ok().body(courses);
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseDTO courseDTO) {
        Course saved = courseService.addCourse(courseDTO);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/name/{course_name}")
    public ResponseEntity<Course> getCourseByName(@PathVariable String course_name) {
        Course course = courseService.getCourseByName(course_name);
        return ResponseEntity.ok(course);
    }

    @GetMapping(value = "/teacher/name/{teacher_name}")
    public ResponseEntity<List<Course>> getCourseByTeacherName(@PathVariable String teacher_name) {
        List<Course> courseList = courseService.getCoursesByTeacherName(teacher_name);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok().body(courseList);
    }

    @GetMapping(value = "/teacher/id/{teacher_id}")
    public ResponseEntity<List<Course>> getCourseByTeacherId(@PathVariable long teacher_id) {
        List<Course> courseList = courseService.getCoursesByTeacherId(teacher_id);
        if (courseList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok().body(courseList);
    }

    @GetMapping(value = "/presents/{course_id}")
    public ResponseEntity<List<StudentAbsenceCountDTO>> getAbsencesByCourse(@PathVariable long course_id) {
        return ResponseEntity.ok().body(courseService.getPresentsByCourse(course_id));
    }

    @GetMapping(value = "/average/{course_id}")
    public ResponseEntity<Double> getAverageGradeByCourse(@PathVariable long course_id) {
        return ResponseEntity.ok().body(courseService.getAverageGradeByCourse(course_id));
    }
}

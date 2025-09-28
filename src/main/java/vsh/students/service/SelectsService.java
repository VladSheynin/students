package vsh.students.service;

import org.springframework.stereotype.Service;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.repositories.AttendanceRepository;
import vsh.students.repositories.CourseRepository;

import java.util.List;

@Service
public class SelectsService {

    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    public SelectsService(AttendanceRepository attendanceRepository, CourseRepository courseRepository, CourseService courseService) {
        this.attendanceRepository = attendanceRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
    }

    public List<StudentAbsenceCountDTO> getPresentsByCourse(long course_id) {
        if (!courseService.existsById(course_id))
            throw new CourseNotFoundException("Курс с id " + course_id + " не существует");
        return attendanceRepository.findStudentsWithAbsenceCount(courseService.getCourseById(course_id));
    }

    public Double getAverageGradeByCourse(long course_id) {
        if (!courseService.existsById(course_id))
            throw new CourseNotFoundException("Курс с id " + course_id + " не существует");
        return courseRepository.findAverageGradeByCourse(courseService.getCourseById(course_id));
    }
}

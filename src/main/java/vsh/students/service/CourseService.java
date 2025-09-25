package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.dto.CourseDTO;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.CourseRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentsService studentsService;

    /**
     * Добавление курса
     *
     * @param courseDTO - DTO объекта курс
     */
     public void addCourse(CourseDTO courseDTO) {
        if (getCourseByName(courseDTO.getCourseName()) != null) {
            throw new NonUniqueResultException("Такой курс уже есть");
        }

        Teacher teacher;
        try {
            teacher = teacherService.getTeacherById(courseDTO.getTeacherId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Преподаватель с таким ID не найден");
        }

        List<Student> students = new ArrayList<>();
        for (long studentId : courseDTO.getStudentsIds()) {
            try {
                students.add(studentsService.getStudentById(studentId));
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Студент с ID " + studentId + " не найден");
            }
        }

        Course course = new Course();
        course.setName(courseDTO.getCourseName());
        course.setTeacher(teacher);
        course.setStudents(students);
        courseRepository.save(course);
    }

    /**
     * Получение курса по его Id
     *
     * @param id - идентификатор курса
     * @return - полученный курс
     */
    public Course getCourseById(long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        List<Course> courseList = optionalCourse.map(List::of).orElseGet(List::of);
        if (courseList.isEmpty()) throw new EntityNotFoundException("Курс не найден");
        return courseList.getFirst();
    }

    /**
     * Получение курса по названию
     *
     * @param name - название курса
     * @return - полученный курс
     */
    public Course getCourseByName(String name) {
        return courseRepository.findByNameWithTeacherAndStudents(name);
    }

    /**
     * Получение списка курсов по имени преподавателя
     *
     * @param name - имя преподавателя
     * @return - список курсов данного преподавателя
     */
    public List<Course> getCoursesByTeacherName(String name) {
        return courseRepository.findByTeacher_Name(name);
    }

    /**
     * Получение списка курсов по id преподавателя
     *
     * @param id - идентификатор преподавателя
     * @return - список курсов данного преподавателя
     */
    public List<Course> getCoursesByTeacherId(long id) {
        return courseRepository.findByTeacher_Id(id);
    }

    /**
     * Получение всех курсов
     *
     * @return - полный список курсов
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}

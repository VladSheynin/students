package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
     * @param course_name  имя курса
     * @param teacher_id   id преподавателя
     * @param students_ids список id студентов
     */
    public void addCourse(String course_name, long teacher_id, String students_ids) {
        if (getCourseByName(course_name) != null) throw new NonUniqueResultException("Такой курс уже есть");
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherById(teacher_id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Один из идентификаторов преподавателя не существует");
        }
        List<String> strings = Arrays.stream(students_ids.trim().split(",")).toList();
        List<Student> students = new ArrayList<>();
        for (String string : strings) {
            try {
                students.add(studentsService.getStudentById(Long.parseLong(string)));
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Один из идентификаторов студентов не существует");
            }
        }
        Course course = new Course();
        course.setTeacher(teacher);
        course.setName(course_name);
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
        return courseRepository.findByName(name);
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

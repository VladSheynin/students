package vsh.students.service;

import org.springframework.stereotype.Service;
import vsh.students.dto.CourseDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.DuplicateCourseException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.CourseRepository;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentsService studentsService;

    public CourseService(CourseRepository courseRepository, TeacherService teacherService, StudentsService studentsService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.studentsService = studentsService;
    }

    /**
     * Добавление курса
     *
     * @param courseDTO - DTO объекта курс
     */
    public Course addCourse(CourseDTO courseDTO) {
        if (getCourseByName(courseDTO.getCourseName()) != null) {
            throw new DuplicateCourseException("Курс с именем '" + courseDTO.getCourseName() + "' уже существует");
        }

        Teacher teacher = teacherService.getTeacherById(courseDTO.getTeacherId());

        List<Student> students = studentsService.getStudentsByIds(courseDTO.getStudentsIds());
        if (students.size() != courseDTO.getStudentsIds().size()) {
            List<Long> foundIds = students.stream().map(Student::getId).toList();
            List<Long> missingIds = courseDTO.getStudentsIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new StudentNotFoundException("Не найдены студенты с id: " + missingIds);
        }


        Course course = new Course();
        course.setName(courseDTO.getCourseName());
        course.setTeacher(teacher);
        course.setStudents(students);
        courseRepository.save(course);
        return course;
    }

    /**
     * Получение курса по его Id
     *
     * @param id - идентификатор курса
     * @return - полученный курс
     */
    public Course getCourseById(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Курс с ID " + id + " не найден"));
    }

    /**
     * Получение курса по названию
     *
     * @param name - название курса
     * @return - полученный курс
     */
    public Course getCourseByName(String name) {
        Course course = courseRepository.findByNameWithTeacherAndStudents(name);
        if (course == null) {
            throw new CourseNotFoundException("Курс с названием '" + name + "' не найден");
        }
        return course;
    }

    /**
     * Получение списка курсов по имени преподавателя
     *
     * @param name - имя преподавателя
     * @return - список курсов данного преподавателя
     */
    public List<Course> getCoursesByTeacherName(String name) {
        if (teacherService.getTeacherByName(name) == null)
            throw new TeacherNotFoundException("Преподаватель с именем '" + name + "' не найден");
        return courseRepository.findByTeacher_Name(name);
    }

    /**
     * Получение списка курсов по id преподавателя
     *
     * @param id - идентификатор преподавателя
     * @return - список курсов данного преподавателя
     */
    public List<Course> getCoursesByTeacherId(long id) {
        if (teacherService.getTeacherById(id) == null)
            throw new TeacherNotFoundException("Преподаватель с id '" + id + "' не найден");
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

    /**
     * Проверка наличия курса с id
     *
     * @param id - конкретный id
     * @return - true|false если курс есть в базе
     */
    public boolean existsById(long id) {
        return courseRepository.existsById(id);
    }
}

package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.repositories.GradeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradeService {
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentsService studentsService;


    /**
     * Добавление оценки
     *
     * @param student_id - идентификатор студента
     * @param course_id  - идентификатор курса
     * @param gradeSize  - оценка
     * @param gradeAt    - дата постановки оценки
     */
    public void addGrade(long student_id, long course_id, int gradeSize, LocalDate gradeAt) {
        Student student;
        try {
            student = studentsService.getStudentById(student_id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Студент не найден");
        }
        Course course;
        try {
            course = courseService.getCourseById(course_id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Курс не найден");
        }
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setGrade(gradeSize);
        grade.setGradedAt(gradeAt);
        gradeRepository.save(grade);
    }

    /**
     * Получение оценок по студенту
     *
     * @param id - идентификатор студента
     * @return - полученные оценки
     */
    public List<Grade> getGradeByStudentId(long id) {
        return gradeRepository.findByStudent_Id(id);
    }

    /**
     * Получение оценок по курсу
     *
     * @param id - идентификатор курса
     * @return - полученные оценки
     */
    public List<Grade> getGradeByCourseId(long id) {
        return gradeRepository.findByCourse_Id(id);
    }

}

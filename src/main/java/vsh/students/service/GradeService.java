package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.dto.GradeDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.repositories.GradeRepository;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;
    private final CourseService courseService;
    private final StudentsService studentsService;

    public GradeService(GradeRepository gradeRepository, CourseService courseService, StudentsService studentsService) {
        this.gradeRepository = gradeRepository;
        this.courseService = courseService;
        this.studentsService = studentsService;
    }

    /**
     * Добавление оценки
     *
     * @param gradeDTO - DTO оценки
     */
    public Grade addGrade(GradeDTO gradeDTO) {
        Student student;
        student = studentsService.getStudentById(gradeDTO.getStudent_id());

        Course course;
        course = courseService.getCourseById(gradeDTO.getCourse_id());

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setGrade(gradeDTO.getGradeSize());
        grade.setGradedAt(gradeDTO.getGradeAt());
        gradeRepository.save(grade);
        return grade;
    }

    /**
     * Получение оценок по студенту
     *
     * @param id - идентификатор студента
     * @return - полученные оценки
     */
    public List<Grade> getGradeByStudentId(long id) {
        if (!studentsService.existsById(id))
            throw new StudentNotFoundException("Студент с ID " + id + " не найден");
        return gradeRepository.findByStudent_Id(id).orElse(List.of());
    }

    /**
     * Получение оценок по курсу
     *
     * @param id - идентификатор курса
     * @return - полученные оценки
     */
    public List<Grade> getGradeByCourseId(long id) {
        if (!courseService.existsById(id)) {
            throw new CourseNotFoundException("Курс с ID " + id + " не найден");
        }
        return gradeRepository.findByCourse_Id(id).orElse(List.of());
    }

}

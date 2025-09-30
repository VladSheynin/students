package vsh.students.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vsh.students.dto.GradeDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.LowAttendanceException;
import vsh.students.exception.NoAttendanceException;
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
    private final AttendanceService attendanceService;
    //минимальный процент посещений курса при котором ставится оценка
    private final static double PERCENT_FOR_ATTENDANCE = 60;

    public GradeService(GradeRepository gradeRepository, CourseService courseService, StudentsService studentsService, AttendanceService attendanceService) {
        this.gradeRepository = gradeRepository;
        this.courseService = courseService;
        this.studentsService = studentsService;
        this.attendanceService = attendanceService;
    }

    /**
     * Добавление оценки
     *
     * @param gradeDTO - DTO оценки
     */
    @Transactional
    public Grade addGrade(GradeDTO gradeDTO) {
        Student student;
        student = studentsService.getStudentById(gradeDTO.getStudent_id());

        Course course;
        course = courseService.getCourseById(gradeDTO.getCourse_id());

        check(student, course);

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setGrade(gradeDTO.getGradeSize());
        grade.setGradedAt(gradeDTO.getGradeAt());
        gradeRepository.save(grade);
        return grade;
    }

    /**
     * Проверка посещаемости (если процент посещаемости меньше числа PERCENT_FOR_ATTENDANCE, то оценка не ставится и выбрасывается исключение
     *
     * @param student - студент
     * @param course  - курс
     */
    void check(Student student, Course course) {
        StudentAttendanceCountDTO studentAttendanceCountDTO = attendanceService.getStudentAttendanceByCourse(student.getId(), course.getId());
        long present = studentAttendanceCountDTO.getPresent();
        long absences = studentAttendanceCountDTO.getAbsences();

        if (present + absences == 0) {
            throw new NoAttendanceException("Студент не посещал курс " + course.getName());
        }

        double percent = present * 100.0 / (present + absences);

        if (percent < PERCENT_FOR_ATTENDANCE) {
            throw new LowAttendanceException("Студент " + student.getName() + " на курсе " + course.getName() +
                    " посетил всего " + present + " занятий из " + (present + absences) + " (это " + percent + "%), при минимуме " + PERCENT_FOR_ATTENDANCE + "%");
        }

    }

    /**
     * Получение оценок по студенту
     *
     * @param id - идентификатор студента
     * @return - полученные оценки
     */
    public List<Grade> getGradeByStudentId(long id) {
        if (!studentsService.existsById(id)) throw new StudentNotFoundException("Студент с ID " + id + " не найден");
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

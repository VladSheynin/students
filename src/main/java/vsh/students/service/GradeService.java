package vsh.students.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vsh.students.dto.GradeDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.exception.*;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.repositories.GradeRepository;

import java.util.List;

@Service
public class GradeService {
    //минимальный процент посещений курса при котором ставится оценка
    private final static double PERCENT_FOR_ATTENDANCE = 60;
    private final GradeRepository gradeRepository;
    private final CourseService courseService;
    private final StudentsService studentsService;
    private final AttendanceService attendanceService;

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

        // проверка, что у данного студента еще нет оценки по этому курсу
        if (getGradeByStudentIdAndCourse(gradeDTO.getStudent_id(), gradeDTO.getCourse_id()).isEmpty()) {
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
        } else throw new DuplicateGradeOnCourseException("У данного студента уже есть оценка за этот курс ");
    }

    /**
     * Проверка посещаемости (если процент посещаемости меньше числа PERCENT_FOR_ATTENDANCE, то оценка не ставится и выбрасывается исключение
     *
     * @param student - студент
     * @param course  - курс
     */
    private void check(Student student, Course course) {
        StudentAttendanceCountDTO studentAttendanceCountDTO = attendanceService.getStudentAttendanceByCourse(student.getId(), course.getId());
        long present = studentAttendanceCountDTO.getPresent();
        long absences = studentAttendanceCountDTO.getAbsences();

        if (present + absences == 0) {
            throw new NoAttendanceException("Студент не посещал курс " + course.getName());
        }

        double percent = present * 100.0 / (present + absences);

        if (percent < PERCENT_FOR_ATTENDANCE) {
            throw new LowAttendanceException("Студент " + student.getName() + " на курсе " + course.getName() + " посетил всего " + present + " занятий из " + (present + absences) + " (это " + percent + "%), при минимуме " + PERCENT_FOR_ATTENDANCE + "%");
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
     * Получение оценок по студенту и курсу одновременно
     *
     * @param student_id - идентификатор студента
     * @param course_id  - идентификатор курса
     * @return - полученные оценки
     */
    public List<Grade> getGradeByStudentIdAndCourse(long student_id, long course_id) {
        if (!studentsService.existsById(student_id))
            throw new StudentNotFoundException("Студент с ID " + student_id + " не найден");
        if (!courseService.existsById(course_id))
            throw new CourseNotFoundException("Курс с ID " + course_id + " не найден");
        return gradeRepository.findByStudent_idAndCourse_Id(student_id, course_id).orElse(List.of());
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

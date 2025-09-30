package vsh.students.service;

import org.springframework.stereotype.Service;
import vsh.students.dto.AttendanceDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.NoAttendanceException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.repositories.AttendanceRepository;

import java.util.List;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final CourseService courseService;
    private final StudentsService studentsService;

    public AttendanceService(AttendanceRepository attendanceRepository, CourseService courseService, StudentsService studentsService) {
        this.attendanceRepository = attendanceRepository;
        this.courseService = courseService;
        this.studentsService = studentsService;
    }

    /**
     * Добавление факта посещения
     *
     * @param attendanceDTO - DTO факта посещения
     */
    public Attendance addAttendance(AttendanceDTO attendanceDTO) {
        Student student;
        student = studentsService.getStudentById(attendanceDTO.getStudent_id());

        Course course;
        course = courseService.getCourseById(attendanceDTO.getCourse_id());

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(attendanceDTO.getDate());
        attendance.setPresent(attendanceDTO.isPresent());
        attendanceRepository.save(attendance);
        return attendance;
    }

    /**
     * Получение всех посещений студента
     *
     * @param id - идентификатор студента
     * @return - посещения данного студента
     */
    public List<Attendance> getAttendanceByStudentId(long id) {
        if (!studentsService.existsById(id))
            throw new StudentNotFoundException("\"Студент с ID \"" + id + "\" не найден\"");
        return attendanceRepository.findByStudent_Id(id);
    }

    /**
     * Посещаемость студентом занятия по крусу
     *
     * @param student_id - идентификатор студента
     * @param course_id  - идентификатор курса
     * @return - объект с параметрами посещений и прогулов по данному курсу
     */
    public StudentAttendanceCountDTO getStudentAttendanceByCourse(long student_id, long course_id) {
        if (!studentsService.existsById(student_id))
            throw new StudentNotFoundException(" Студент с id " + student_id + " не найден");
        if (!courseService.existsById(student_id))
            throw new CourseNotFoundException("Курс с ID " + course_id + " не найден");
        StudentAttendanceCountDTO result = attendanceRepository.getStudentAttendance(studentsService.getStudentById(student_id), courseService.getCourseById(course_id));
        if (result == null) throw new NoAttendanceException("Студент не посещал данный курс (нет фактов посещения/пропуска)");
        return result;
    }
}

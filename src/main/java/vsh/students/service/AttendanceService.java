package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.repositories.AttendanceRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentsService studentsService;



    /**
     * Добавление факта посещения
     *
     * @param student_id - идентификатор студента
     * @param course_id  - идентификатор курса
     * @param attendance_date  - дата посещения
     * @param present    - факт посещения
     */
    public void addAttendance(long student_id, long course_id, LocalDate attendance_date, boolean present) {
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
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(attendance_date);
        attendance.setPresent(present);
        attendanceRepository.save(attendance);
    }

    /**
     * Получение всех посещений студента
     *
     * @param id - идентификатор студента
     * @return - посещения данного студента
     */
    public List<Attendance> getAttendanceByStudentId(long id) {
        return attendanceRepository.findByStudent_Id(id);
    }

}

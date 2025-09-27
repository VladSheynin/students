package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.dto.AttendanceDTO;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.repositories.AttendanceRepository;

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
     * @param attendanceDTO - DTO факта посещения
     */
    public Attendance addAttendance(AttendanceDTO attendanceDTO) {//long student_id, long course_id, LocalDate attendance_date, boolean present) {
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
            throw new StudentNotFoundException("\"Студент с ID \" + id + \" не найден\"");
        return attendanceRepository.findByStudent_Id(id);
    }

}

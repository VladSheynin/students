package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent_Id(long id);

    @Query("""
                 SELECT a.student, COUNT(a)\s
                 FROM Attendance a\s
                 WHERE a.course = :course AND a.present = false\s
                 GROUP BY a.student
            \s""")
    List<StudentAbsenceCountDTO> findStudentsWithAbsenceCount(Course course);

    @Query("""
                SELECT a.student,\s
                    SUM(CASE WHEN a.present = true THEN 1 ELSE 0 END),\s
                    SUM(CASE WHEN a.present = false THEN 1 ELSE 0 END)\s
                FROM Attendance a\s
                WHERE a.course = :course AND a.student = :student\s
                GROUP BY a.student
            \s""")
    StudentAttendanceCountDTO getStudentAttendance(Student student, Course course);

}

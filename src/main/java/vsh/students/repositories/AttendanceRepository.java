package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.model.Attendance;
import vsh.students.model.Course;

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
}

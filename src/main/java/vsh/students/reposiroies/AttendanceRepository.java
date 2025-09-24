package vsh.students.reposiroies;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Attendance;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent_Id(long id);
}

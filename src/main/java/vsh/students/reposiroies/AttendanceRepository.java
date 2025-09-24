package vsh.students.reposiroies;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}

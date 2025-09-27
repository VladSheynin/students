package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByName(String name);

    List<Teacher> findByDepartment(String department);
}

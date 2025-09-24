package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Teacher;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByName(String name);

    List<Teacher> findByDepartment(String department);
}

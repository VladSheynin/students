package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByName(String name);

    List<Student> findByGroup(String group);
}

package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByName(String name);

    List<Student> findByGroup(String group);

    List<Student> findAllByIdIn(List<Long> ids);
}

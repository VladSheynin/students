package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<List<Grade>> findByStudent_Id(long id);

    Optional<List<Grade>> findByCourse_Id(long id);
}

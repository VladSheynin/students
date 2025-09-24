package vsh.students.reposiroies;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}

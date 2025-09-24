package vsh.students.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Grade;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent_Id(long id);

    List<Grade> findByCourse_Id(long id);
}

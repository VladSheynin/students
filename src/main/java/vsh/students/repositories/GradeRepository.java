package vsh.students.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vsh.students.model.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    @EntityGraph(attributePaths = {"teacher", "students"})
    Optional<List<Grade>> findByStudent_Id(long id);

    @EntityGraph(attributePaths = {"teacher", "students"})
    Optional<List<Grade>> findByCourse_Id(long id);

    @EntityGraph(attributePaths = {"teacher", "students"})
    Optional<List<Grade>> findByStudent_idAndCourse_Id(long student_id,long course_id);
}

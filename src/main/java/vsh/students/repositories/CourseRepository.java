package vsh.students.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vsh.students.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @EntityGraph(attributePaths = {"teacher", "students"})
    List<Course> findAll();

    @EntityGraph(attributePaths = {"teacher", "students"})
    List<Course> findByTeacher_Name(String name);

    @EntityGraph(attributePaths = {"teacher", "students"})
    List<Course> findByTeacher_Id(long id);

    @Query("SELECT c FROM Course c JOIN FETCH c.teacher JOIN FETCH c.students WHERE c.name = :name")
    Course findByNameWithTeacherAndStudents(@Param("name") String name);

}

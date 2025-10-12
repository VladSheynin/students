package vsh.students.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.CourseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    StudentsService studentsService;
    @Mock
    TeacherService teacherService;
    @Mock
    CourseRepository courseRepository;
    @InjectMocks
    CourseService courseService;

    @BeforeEach
    void setUp() {


    }
}
/*    @Test
    void getGradeByCourseId_ShouldReturnCourseWhenCourseExist() {




    }

}*/


/*

    @Test
    void addGrade() {
    }

    @Test
    void getGradeByStudentId() {
    }

    @Test
    void getGradeByStudentIdAndCourse() {
    }

    @Test
    void getGradeByCourseId() {
    }
*/
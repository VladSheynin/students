package vsh.students.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.model.Teacher;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Testcontainers
class CourseRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GradeRepository gradeRepository;

    private Student studentSheynin;
    private Student studentIvanov;
    private Course courseEnglish;
    private Course courseProg;
    private Course courseMath;
    private Teacher teacherAbarnikova;
    private Teacher teacherVorotnikov;
    private Grade gradeProg1;
    private Grade gradeProg2;
    private Grade gradeProg3;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @BeforeEach
    void setUp() {
        teacherAbarnikova = new Teacher();
        teacherAbarnikova.setName("Abarnikova");
        teacherAbarnikova.setDepartment("VM");
        teacherRepository.save(teacherAbarnikova);

        teacherVorotnikov = new Teacher();
        teacherVorotnikov.setName("Vorotnikov");
        teacherVorotnikov.setDepartment("Math");
        teacherRepository.save(teacherVorotnikov);

        studentSheynin = new Student();
        studentSheynin.setName("Sheynin");
        studentSheynin.setGroup("1VM-2");
        studentSheynin = studentRepository.save(studentSheynin);

        studentIvanov = new Student();
        studentIvanov.setName("Ivanov");
        studentIvanov.setGroup("1VM-1");
        studentIvanov = studentRepository.save(studentIvanov);

        courseEnglish = new Course();
        courseEnglish.setName("English");
        courseEnglish.setTeacher(teacherAbarnikova);
        courseEnglish.setStudents(List.of(studentSheynin));
        courseEnglish = courseRepository.save(courseEnglish);

        courseProg = new Course();
        courseProg.setName("Programming");
        courseProg.setTeacher(teacherAbarnikova);
        courseProg.setStudents(List.of(studentSheynin, studentIvanov));
        courseProg = courseRepository.save(courseProg);

        courseMath = new Course();
        courseMath.setName("Mathematics");
        courseMath.setTeacher(teacherVorotnikov);
        courseMath.setStudents(List.of(studentIvanov));
        courseMath = courseRepository.save(courseMath);

        gradeProg1 = new Grade();
        gradeProg1.setStudent(studentSheynin);
        gradeProg1.setCourse(courseProg);
        gradeProg1.setGrade(5);
        gradeProg1.setGradedAt(LocalDate.now().minusDays(1));
        gradeRepository.save(gradeProg1);

        gradeProg2 = new Grade();
        gradeProg2.setStudent(studentSheynin);
        gradeProg2.setCourse(courseProg);
        gradeProg2.setGrade(4);
        gradeProg2.setGradedAt(LocalDate.now().minusDays(2));
        gradeRepository.save(gradeProg2);

        gradeProg3 = new Grade();
        gradeProg3.setStudent(studentIvanov);
        gradeProg3.setCourse(courseProg);
        gradeProg3.setGrade(3);
        gradeProg3.setGradedAt(LocalDate.now().minusDays(1));
        gradeRepository.save(gradeProg3);
    }

    @Test
    void findByTeacher_Name() {
    }

    @Test
    void findByTeacher_Id() {
        List<Course> courseList = courseRepository.findByTeacher_Id(teacherAbarnikova.getId());
        assertThat(courseList).hasSize(2);
        assertThat(courseList.get(0).getName()).isEqualTo(courseEnglish.getName());
        assertThat(courseList.get(0).getTeacher()).isEqualTo(teacherAbarnikova);
        assertThat(courseList.get(0).getStudents()).contains(studentSheynin);
        assertThat(courseList.get(1).getName()).isEqualTo(courseProg.getName());
        assertThat(courseList.get(1).getStudents()).contains(studentSheynin, studentIvanov);
    }

    @Test
    void findByTeacher_Id_NoSuchTeacherId() {
        List<Course> courseList = courseRepository.findByTeacher_Id(123L);
        assertThat(courseList).hasSize(0);
    }

    @Test
    void findByNameWithTeacherAndStudents() {
        Course course = courseRepository.findByNameWithTeacherAndStudents(courseProg.getName());
        assertThat(course.getName()).isEqualTo("Programming");
        assertThat(course.getTeacher()).isEqualTo(teacherAbarnikova);
        assertThat(course.getStudents()).contains(studentSheynin, studentIvanov);
    }

    @Test
    void findByNameWithTeacherAndStudents_NoSuchCourseName() {
        Course course = courseRepository.findByNameWithTeacherAndStudents("NoCourse");
        assertThat(course).isNull();
    }

    @Test
    void findAverageGradeByCourse() {
        Double averageGrade = courseRepository.findAverageGradeByCourse(courseProg);
        assertThat(averageGrade).isEqualTo(4D);
    }
}
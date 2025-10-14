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
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@Testcontainers
class AttendanceRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    private Student studentSheynin;
    private Student studentIvanov;
    private Course courseEnglish;
    private Teacher teacherAbarnikova;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @BeforeEach
    void setUp() {
        studentSheynin = new Student();
        studentSheynin.setName("Sheynin");
        studentSheynin.setGroup("1VM-2");
        studentSheynin = studentRepository.save(studentSheynin);

        studentIvanov = new Student();
        studentIvanov.setName("Ivanov");
        studentIvanov.setGroup("1VM-1");
        studentIvanov = studentRepository.save(studentIvanov);

        teacherAbarnikova = new Teacher();
        teacherAbarnikova.setName("Abarnikova");
        teacherAbarnikova.setDepartment("VM");
        teacherAbarnikova = teacherRepository.save(teacherAbarnikova);

        courseEnglish = new Course();
        courseEnglish.setName("English");
        courseEnglish.setTeacher(teacherAbarnikova);
        courseEnglish.setStudents(List.of(studentSheynin, studentIvanov));
        courseEnglish = courseRepository.save(courseEnglish);

        Attendance attendance1 = new Attendance();
        attendance1.setStudent(studentSheynin);
        attendance1.setCourse(courseEnglish);
        attendance1.setPresent(true);
        attendance1.setDate(LocalDate.now());
        attendance1 = attendanceRepository.save(attendance1);

        Attendance attendance2 = new Attendance();
        attendance2.setStudent(studentSheynin);
        attendance2.setCourse(courseEnglish);
        attendance2.setPresent(true);
        attendance2.setDate(LocalDate.now().minusDays(1));
        attendance2 = attendanceRepository.save(attendance2);

        Attendance attendance3 = new Attendance();
        attendance3.setStudent(studentIvanov);
        attendance3.setCourse(courseEnglish);
        attendance3.setPresent(false);
        attendance3.setDate(LocalDate.now());
        attendance3 = attendanceRepository.save(attendance3);

        Attendance attendance4 = new Attendance();
        attendance4.setStudent(studentIvanov);
        attendance4.setCourse(courseEnglish);
        attendance4.setPresent(true);
        attendance4.setDate(LocalDate.now().minusDays(1));
        attendance4 = attendanceRepository.save(attendance4);

    }

    @Test
    void findByStudent_Id() {
        List<Attendance> attendances = attendanceRepository.findByStudent_Id(studentSheynin.getId());
        assertThat(attendances).hasSize(2);

        assertThat(attendances.get(0).getStudent()).isEqualTo(studentSheynin);
        assertThat(attendances.get(0).getCourse()).isEqualTo(courseEnglish);
        assertThat(attendances.get(0).getDate()).isEqualTo(LocalDate.now());
        assertThat(attendances.get(0).isPresent()).isTrue();

        assertThat(attendances.get(1).getStudent()).isEqualTo(studentSheynin);
        assertThat(attendances.get(1).getDate()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(attendances.get(1).isPresent()).isTrue();
    }

    @Test
    void findByStudent_Id_NoSuchStudentId() {
        List<Attendance> attendances = attendanceRepository.findByStudent_Id(123L);
        assertThat(attendances).hasSize(0);
    }


    @Test
    void findStudentsWithAbsenceCount() {
        List<StudentAbsenceCountDTO> studentAttendanceCountDTOList = attendanceRepository.findStudentsWithAbsenceCount(courseEnglish);
        assertThat(studentAttendanceCountDTOList).hasSize(1);
        assertThat(studentAttendanceCountDTOList.getFirst().getStudent()).isEqualTo(studentIvanov);
    }

    @Test
    void getStudentAttendance() {
        StudentAttendanceCountDTO studentAttendanceCountDTO = attendanceRepository.getStudentAttendance(studentSheynin, courseEnglish);
        assertThat(studentAttendanceCountDTO.getStudent()).isEqualTo(studentSheynin);
        assertThat(studentAttendanceCountDTO.getAbsences()).isEqualTo(0);
        assertThat(studentAttendanceCountDTO.getPresent()).isEqualTo(2);
    }
}
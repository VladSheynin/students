package vsh.students.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import vsh.students.dto.GradeDTO;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.AttendanceRepository;
import vsh.students.repositories.CourseRepository;
import vsh.students.repositories.StudentRepository;
import vsh.students.repositories.TeacherRepository;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@Transactional
@AutoConfigureMockMvc
public class GradesIntegrationTest {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_grades_db")
            .withUsername("user")
            .withPassword("password");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }


    long idFirstStudent;
    long idFirstCourse;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void addThreeGradesWithStudentAndCourse() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        student.setGroup("TestGroup");
        student = studentRepository.save(student);
        Student student2 = new Student();
        student2.setName("SecondTestStudent");
        student2.setGroup("SecondTestGroup");
        student2 = studentRepository.save(student2);
        Teacher teacher = new Teacher();
        teacher.setDepartment("TestDepartment");
        teacher.setName("TestTeacher");
        teacher = teacherRepository.save(teacher);
        Course course = new Course();
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student, student2));
        course = courseRepository.save(course);
        Course course2 = new Course();
        course2.setName("AnotherTestCourse");
        course2.setTeacher(teacher);
        course2.setStudents(List.of(student, student2));
        course2 = courseRepository.save(course2);
        Attendance attendance1 = new Attendance();
        attendance1.setStudent(student);
        attendance1.setPresent(true);
        attendance1.setCourse(course);
        attendance1.setDate(LocalDate.now());
        attendanceRepository.save(attendance1);
        Attendance attendance2 = new Attendance();
        attendance2.setStudent(student);
        attendance2.setPresent(true);
        attendance2.setCourse(course);
        attendance2.setDate(LocalDate.now().minusDays(1));
        attendanceRepository.save(attendance2);
        Attendance attendance3 = new Attendance();
        attendance3.setStudent(student2);
        attendance3.setPresent(true);
        attendance3.setCourse(course);
        attendance3.setDate(LocalDate.now().minusDays(1));
        attendanceRepository.save(attendance3);
        Attendance attendance4 = new Attendance();
        attendance4.setStudent(student);
        attendance4.setPresent(true);
        attendance4.setCourse(course2);
        attendance4.setDate(LocalDate.now().minusDays(1));
        attendanceRepository.save(attendance4);
        Attendance attendance5 = new Attendance();
        attendance5.setStudent(student);
        attendance5.setPresent(true);
        attendance5.setCourse(course2);
        attendance5.setDate(LocalDate.now().minusDays(1));
        attendanceRepository.save(attendance5);

        //TestStudent, оценка за сегодня, 5
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setStudent_id(student.getId());
        gradeDTO.setGradeAt(LocalDate.now());
        gradeDTO.setCourse_id(course.getId());
        gradeDTO.setGradeSize(5);
        MvcResult result = mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        idFirstStudent = student.getId();
        idFirstCourse = course.getId();

        //TestStudent, оценка за вчера, 4, курс AnotherTestCourse
        gradeDTO.setCourse_id(course2.getId());
        gradeDTO.setGradeSize(4);
        gradeDTO.setGradeAt(LocalDate.now().minusDays(1));
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeDTO)))
                .andExpect(status().isOk())
                .andReturn();

        //SecondTestStudent, оценка за сегодня, 4
        gradeDTO.setStudent_id(student2.getId());
        gradeDTO.setCourse_id(course.getId());
        gradeDTO.setGradeSize(4);
        gradeDTO.setGradeAt(LocalDate.now().minusDays(1));
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeDTO)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getGradeByStudentId() throws Exception {
        mockMvc.perform(get("/grades/student/" + idFirstStudent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].student.name").value("TestStudent"))
                .andExpect(jsonPath("$[0].course.name").value("TestCourse"))
                .andExpect(jsonPath("$[0].gradedAt").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].grade").value(5))
                .andExpect(jsonPath("$[1].student.name").value("TestStudent"))
                .andExpect(jsonPath("$[1].course.name").value("AnotherTestCourse"))
                .andExpect(jsonPath("$[1].gradedAt").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("$[1].grade").value(4));
    }

    @Test
    void getGradeByCourse() throws Exception {
        mockMvc.perform(get("/grades/course/" + idFirstCourse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].student.name").value("TestStudent"))
                .andExpect(jsonPath("$[0].course.name").value("TestCourse"))
                .andExpect(jsonPath("$[0].gradedAt").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].grade").value(5))
                .andExpect(jsonPath("$[1].student.name").value("SecondTestStudent"))
                .andExpect(jsonPath("$[1].course.name").value("TestCourse"))
                .andExpect(jsonPath("$[1].gradedAt").value(LocalDate.now().minusDays(1).toString()))
                .andExpect(jsonPath("$[1].grade").value(4));
    }


}

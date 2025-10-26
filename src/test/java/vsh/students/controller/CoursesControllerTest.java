package vsh.students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vsh.students.dto.CourseDTO;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.CourseRepository;
import vsh.students.service.CourseService;
import vsh.students.service.GradeService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CoursesController.class)
class CoursesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getCourseById_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student));

        when(courseService.getCourseById(1L)).thenReturn(course);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCourse"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$.students[0].name").value("TestStudent"));
    }

    @Test
    void getCourseByName_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student));

        when(courseService.getCourseByName("TestCourse")).thenReturn(course);

        mockMvc.perform(get("/courses/name/TestCourse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCourse"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$.students[0].name").value("TestStudent"));
    }

    @Test
    void getAllCourses_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student));
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("AnotherTestCourse");
        course2.setTeacher(teacher);
        course2.setStudents(List.of(student));

        when(courseService.getAllCourses()).thenReturn(List.of(course,course2));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestCourse"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$[0].students[0].name").value("TestStudent"))
                .andExpect(jsonPath("$[1].name").value("AnotherTestCourse"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$[1].students[0].name").value("TestStudent"));
    }

    @Test
    void getCoursesByTeacherName_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");
        Teacher teacher2 = new Teacher();
        teacher2.setName("SecondTestTeacher");

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student));
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("SecondTestCourse");
        course2.setTeacher(teacher2);
        course2.setStudents(List.of(student));
        Course course3 = new Course();
        course3.setId(3L);
        course3.setName("ThirdTestCourse");
        course3.setTeacher(teacher);
        course3.setStudents(List.of(student));

        when(courseService.getCoursesByTeacherName("TestTeacher")).thenReturn(List.of(course,course3));

        mockMvc.perform(get("/courses/teacher/name/TestTeacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestCourse"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$[1].name").value("ThirdTestCourse"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].teacher.name").value("TestTeacher"));
    }

    @Test
    void getCoursesByTeacherId_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");
        teacher.setId(10L);
        Teacher teacher2 = new Teacher();
        teacher2.setName("SecondTestTeacher");
        teacher2.setId(20L);

        Course course = new Course();
        course.setId(1L);
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setStudents(List.of(student));
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("SecondTestCourse");
        course2.setTeacher(teacher2);
        course2.setStudents(List.of(student));
        Course course3 = new Course();
        course3.setId(3L);
        course3.setName("ThirdTestCourse");
        course3.setTeacher(teacher);
        course3.setStudents(List.of(student));

        when(courseService.getCoursesByTeacherId(10L)).thenReturn(List.of(course,course3));

        mockMvc.perform(get("/courses/teacher/id/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestCourse"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].teacher.name").value("TestTeacher"))
                .andExpect(jsonPath("$[1].name").value("ThirdTestCourse"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].teacher.name").value("TestTeacher"));
    }

    @Test
    void getAbsencesByCourse_ShouldReturnAverageGrade() throws Exception {
        when(courseService.getAverageGradeByCourse(1L)).thenReturn(4D);
        mockMvc.perform(get("/courses//average/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4));
    }

    @Test
    void getAbsencesByCourse_ShouldReturnListStudentAbsence() throws Exception {
        Student student1 = new Student();
        student1.setName("TestStudent");
        student1.setId(10L);
        Student student2 = new Student();
        student2.setName("SecondTestStudent");
        student2.setId(20L);
        StudentAbsenceCountDTO studentAbsenceCountDTO1 =new StudentAbsenceCountDTO(student1,5);
        StudentAbsenceCountDTO studentAbsenceCountDTO2 =new StudentAbsenceCountDTO(student2,4);
        when(courseService.getPresentsByCourse(1L)).thenReturn(List.of(studentAbsenceCountDTO1,studentAbsenceCountDTO2));

        mockMvc.perform(get("/courses/presents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].student.name").value("TestStudent"))
                .andExpect(jsonPath("$[0].absenceCount").value(5))
                .andExpect(jsonPath("$[1].student.name").value("SecondTestStudent"))
                .andExpect(jsonPath("$[1].absenceCount").value(4));
    }

    @Test
    void addCourse_ShouldReturnAddedGrade() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        Teacher teacher = new Teacher();
        teacher.setName("TestTeacher");
        teacher.setId(10L);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("TestCourse");
        courseDTO.setTeacherId(teacher.getId());
        courseDTO.setStudentsIds(List.of(student.getId()));
        Course course = new Course();
        course.setName("TestCourse");
        course.setTeacher(teacher);
        course.setId(100L);
        course.setStudents(List.of(student));

        when(courseService.addCourse(courseDTO)).thenReturn(course);

        String courseJson = objectMapper.writeValueAsString(courseDTO);
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCourse"))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.teacher.id").value(10));
    }
}
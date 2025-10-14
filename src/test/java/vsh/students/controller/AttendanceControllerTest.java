package vsh.students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vsh.students.dto.AttendanceDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AttendanceService attendanceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addAttendance_ShouldReturnAddedAttendance() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Course course = new Course();
        course.setName("TestCourse");
        AttendanceDTO attendanceDTO = new AttendanceDTO();

        Attendance attendance1 = new Attendance();
        attendance1.setStudent(student);
        attendance1.setDate(LocalDate.now());
        attendance1.setId(1L);
        attendance1.setPresent(true);
        attendance1.setCourse(course);

        Mockito.when(attendanceService.addAttendance(any(AttendanceDTO.class))).thenReturn(attendance1);

        String teacherJson = objectMapper.writeValueAsString(attendanceDTO);
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.present").value(true))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.student.name").value("TestStudent"))
                .andExpect(jsonPath("$.course.name").value("TestCourse"));
    }

    @Test
    void getAttendanceByStudentId_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Course course = new Course();
        course.setName("TestCourse");
        Attendance attendance1 = new Attendance();
        attendance1.setStudent(student);
        attendance1.setDate(LocalDate.now());
        attendance1.setId(1L);
        attendance1.setPresent(true);
        attendance1.setCourse(course);
        Attendance attendance2 = new Attendance();
        attendance2.setStudent(student);
        attendance2.setDate(LocalDate.now());
        attendance2.setId(2L);
        attendance2.setPresent(false);
        attendance2.setCourse(course);

        when(attendanceService.getAttendanceByStudentId(1L)).thenReturn(List.of(attendance1, attendance2));

        mockMvc.perform(get("/attendances/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].present").value(true))
                .andExpect(jsonPath("$[1].present").value(false));
    }

    @Test
    void getStudentAttendanceByCourseIdAndStudentId() throws Exception {
        Student student = new Student();
        student.setName("TestStudent");
        Course course = new Course();
        course.setName("TestCourse");

        StudentAttendanceCountDTO studentAttendanceCountDTO1 = new StudentAttendanceCountDTO(student, 3, 30);

        when(attendanceService.getStudentAttendanceByCourse(1L, 10L)).thenReturn(studentAttendanceCountDTO1);

        mockMvc.perform(get("/attendances/student/course/1/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student.name").value("TestStudent"))
                .andExpect(jsonPath("$.present").value(3))
                .andExpect(jsonPath("$.absences").value(30));
    }
}
package vsh.students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vsh.students.dto.GradeDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.service.GradeService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradesController.class)
class GradesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getGradeByStudent_ShouldTestDataResult() throws Exception {
        Grade grade = new Grade();
        grade.setGrade(5);
        Grade grade2 = new Grade();
        grade2.setGrade(4);

        when(gradeService.getGradeByStudentId(1L)).thenReturn(List.of(grade, grade2));

        mockMvc.perform(get("/grades/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].grade").value(5))
                .andExpect(jsonPath("$[1].grade").value(4));
    }

    @Test
    void getGradeByStudent_ShouldExceptionIfStudentNotFound() throws Exception {
        when(gradeService.getGradeByStudentId(2L)).thenThrow(new StudentNotFoundException("Студент с ID 2 не найден"));
        mockMvc.perform(get("/grades/student/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Студент с ID 2 не найден"));
    }

    @Test
    void getGradeByCourse_ShouldTestDataResult() throws Exception {
        Grade grade = new Grade();
        grade.setGrade(5);
        Grade grade2 = new Grade();
        grade2.setGrade(4);
        Grade grade3 = new Grade();

        when(gradeService.getGradeByCourseId(1L)).thenReturn(List.of(grade, grade2));

        mockMvc.perform(get("/grades/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].grade").value(5))
                .andExpect(jsonPath("$[1].grade").value(4));
    }

    @Test
    void getGradeByCourse_ShouldExceptionIfCourseNotFound() throws Exception {
        when(gradeService.getGradeByCourseId(2L)).thenThrow(new CourseNotFoundException("Курс с ID 2 не найден"));
        mockMvc.perform(get("/grades/course/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Курс с ID 2 не найден"));
    }

    @Test
    public void addGrade_ShouldReturnAddedGrade() throws Exception {
        GradeDTO gradeDTO = new GradeDTO();
        Grade grade = new Grade();
        grade.setCourse(new Course());
        grade.setId(1L);
        grade.setGrade(5);
        grade.setGradedAt(LocalDate.now());
        grade.setStudent(new Student());

        Mockito.when(gradeService.addGrade(Mockito.any(GradeDTO.class))).thenReturn(grade);

        String teacherJson = objectMapper.writeValueAsString(gradeDTO);
        mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.grade").value(5))
                .andExpect(jsonPath("$.gradedAt").value(LocalDate.now().toString()));
    }
}
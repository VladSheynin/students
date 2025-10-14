package vsh.students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vsh.students.dto.StudentDTO;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Student;
import vsh.students.service.StudentsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentsController.class)
public class StudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentsService studentsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getStudentById_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        Mockito.when(studentsService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestStudent"));
    }

    @Test
    public void getStudentById_ShouldRightExceptionIfStudentNotFound() throws Exception {
        Mockito.when(studentsService.getStudentById(2L)).thenThrow(new StudentNotFoundException(" Студент с id 2 не найден"));

        mockMvc.perform(get("/students/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(" Студент с id 2 не найден"));
    }

    @Test
    void getStudentByName_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        Mockito.when(studentsService.getStudentByName("TestStudent")).thenReturn(student);

        mockMvc.perform(get("/students/name/TestStudent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestStudent"));
    }

    @Test
    public void getStudentByName_ShouldRightExceptionIfStudentNotFound() throws Exception {
        Mockito.when(studentsService.getStudentByName("NotStudent")).thenThrow(new StudentNotFoundException("Студент с именем 'NotStudent' не найден"));

        mockMvc.perform(get("/students/name/NotStudent"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Студент с именем 'NotStudent' не найден"));
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("SecondTestStudent");
        Mockito.when(studentsService.getAllStudents()).thenReturn(List.of(student, student2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(("$[0].id")).value(1))
                .andExpect(jsonPath(("$[0].name")).value("TestStudent"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath(("$[1].name")).value("SecondTestStudent"));
    }

    @Test
    public void getAllStudents_ShouldRightNo_CONTENTIfStudentsListIsEmpty() throws Exception {
        Mockito.when(studentsService.getAllStudents()).thenReturn(List.of());

        mockMvc.perform(get("/students"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudentsByGroup_ShouldTestDataResult() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        student.setGroup("TestGroup");
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("SecondTestStudent");
        student2.setGroup("SecondTestGroup");
        Student student3 = new Student();
        student3.setId(3L);
        student3.setName("ThirdTestStudent");
        student3.setGroup("SecondTestGroup");
        Mockito.when(studentsService.getStudentsByGroup("SecondTestGroup")).thenReturn(List.of(student2, student3));

        mockMvc.perform(get("/students/group/SecondTestGroup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(("$[0].id")).value(2))
                .andExpect(jsonPath(("$[0].name")).value("SecondTestStudent"))
                .andExpect(jsonPath(("$[0].group")).value("SecondTestGroup"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath(("$[1].name")).value("ThirdTestStudent"))
                .andExpect(jsonPath(("$[1].group")).value("SecondTestGroup"));
    }

    @Test
    public void getStudentsByGroup_ShouldRightNo_CONTENTIfStudentsListIsEmpty() throws Exception {
        Mockito.when(studentsService.getStudentsByGroup(any())).thenReturn(List.of());

        mockMvc.perform(get("/students/group/GroupName"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addStudent_ShouldReturnAddedStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName("TestStudent");
        Student student = new Student();
        student.setId(1L);
        student.setName("TestStudent");
        student.setGroup("TestGroup");
        Mockito.when(studentsService.addStudent(Mockito.any(StudentDTO.class))).thenReturn(student);

        String studentJson = objectMapper.writeValueAsString(studentDTO);
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestStudent"))
                .andExpect(jsonPath("$.group").value("TestGroup"));
    }
}

package vsh.students.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vsh.students.dto.TeacherDTO;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Teacher;
import vsh.students.service.TeacherService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeachersController.class)
class TeachersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getTeacherById_ShouldTestDataResult() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("TestTeacher");
        teacher.setDepartment("TestDepartment");
        Mockito.when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestTeacher"))
                .andExpect(jsonPath("$.department").value("TestDepartment"));
    }

    @Test
    public void getTeacherById_ShouldRightExceptionIfTeacherNotFound() throws Exception {
        Mockito.when(teacherService.getTeacherById(2L)).thenThrow(new TeacherNotFoundException("Преподаватель с id 2 не найден"));

        mockMvc.perform(get("/teachers/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Преподаватель с id 2 не найден"));
    }

    @Test
    void getAllTeachers_ShouldReturnAllTeachers() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("TestTeacher");
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("SecondTestTeacher");
        Mockito.when(teacherService.getAllTeachers()).thenReturn(List.of(teacher, teacher2));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(("$[0].id")).value(1))
                .andExpect(jsonPath(("$[0].name")).value("TestTeacher"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath(("$[1].name")).value("SecondTestTeacher"));
    }

    @Test
    public void getAllTeachers_ShouldRightNo_CONTENTIfTeachersListIsEmpty() throws Exception {
        Mockito.when(teacherService.getAllTeachers()).thenReturn(List.of());

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTeachersByDepartment_ShouldReturnAllTeachersByDepartment() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("AnotherTestDepartment");
        teacher.setDepartment("TestDepartment");
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("SecondTestTeacher");
        teacher2.setDepartment("TestDepartment");
        Teacher teacher3 = new Teacher();
        teacher3.setId(3L);
        teacher3.setName("ThirdTestTeacher");
        teacher3.setDepartment("TestDepartment");
        Mockito.when(teacherService.getTeachersByDepartment("TestDepartment")).thenReturn(List.of(teacher2, teacher3));

        mockMvc.perform(get("/teachers/department/TestDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath(("$[0].id")).value(2))
                .andExpect(jsonPath(("$[0].name")).value("SecondTestTeacher"))
                .andExpect(jsonPath(("$[0].department")).value("TestDepartment"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath(("$[1].name")).value("ThirdTestTeacher"))
                .andExpect(jsonPath("$[1].department").value("TestDepartment"));
    }

    @Test
    void getTeachersByDepartment_ShouldReturnNO_CONTENTIfNoTeacherWithThisDepartment() throws Exception {
        Mockito.when(teacherService.getTeachersByDepartment("NoDepartment")).thenReturn(List.of());

        mockMvc.perform(get("/teachers/department/NoDepartment"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addStudent_ShouldReturnAddedStudent() throws Exception {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setName("TestTeacher");
        teacherDTO.setDepartment("TestDepartment");
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("TestTeacher");
        teacher.setDepartment("TestDepartment");
        Mockito.when(teacherService.addTeacher(Mockito.any(TeacherDTO.class))).thenReturn(teacher);

        String teacherJson = objectMapper.writeValueAsString(teacherDTO);
        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestTeacher"))
                .andExpect(jsonPath("$.department").value("TestDepartment"));
    }
}
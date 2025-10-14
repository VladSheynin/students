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
import vsh.students.dto.TeacherDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeachersIntegrationTest {
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_students_db")
            .withUsername("user")
            .withPassword("password");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    long idFirstTeacher;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void addThreeStudents() throws Exception {
        TeacherDTO teacherDTO1 = new TeacherDTO();
        teacherDTO1.setDepartment("TestDepartment");
        teacherDTO1.setName("FirstTeacher");
        TeacherDTO teacherDTO2 = new TeacherDTO();
        teacherDTO2.setDepartment("AnotherTestDepartment");
        teacherDTO2.setName("SecondTeacher");
        TeacherDTO teacherDTO3 = new TeacherDTO();
        teacherDTO3.setDepartment("TestDepartment");
        teacherDTO3.setName("ThirdTeacher");

        String jsonResponse;

        MvcResult result = mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO1)))
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = result.getResponse().getContentAsString();
        idFirstTeacher = objectMapper.readTree(jsonResponse).get("id").asLong();

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO2)))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO3)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getTeacherById() throws Exception {
        mockMvc.perform(get("/teachers/" + idFirstTeacher))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FirstTeacher"))
                .andExpect(jsonPath("$.department").value("TestDepartment"));
    }

    @Test
    public void getUnrealTeacherCheckException() throws Exception {
        mockMvc.perform(get("/teachers/777"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Преподаватель с id 777 не найден"));
    }

    @Test
    public void getAllTeachers() throws Exception {
        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getTeachersByDepartment() throws Exception {
        mockMvc.perform(get("/teachers/department/TestDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

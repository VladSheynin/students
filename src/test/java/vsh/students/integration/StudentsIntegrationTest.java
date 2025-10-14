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
import vsh.students.dto.StudentDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentsIntegrationTest {
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

    long idFirstStudent;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void addThreeStudents() throws Exception {
        StudentDTO studentDTO1 = new StudentDTO();
        studentDTO1.setName("FirstStudent");
        studentDTO1.setGroup("TestGroup");
        StudentDTO studentDTO2 = new StudentDTO();
        studentDTO2.setName("SecondStudent");
        studentDTO2.setGroup("AnotherTestGroup");
        StudentDTO studentDTO3 = new StudentDTO();
        studentDTO3.setName("ThirdStudent");
        studentDTO3.setGroup("TestGroup");
        String jsonResponse;

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO1)))
                .andExpect(status().isOk())
                .andReturn();
        jsonResponse = result.getResponse().getContentAsString();
        idFirstStudent = objectMapper.readTree(jsonResponse).get("id").asLong();

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO2)))
                .andExpect(status().isOk())
                .andReturn();


        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO3)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getStudentByName() throws Exception {
        mockMvc.perform(get("/students/name/SecondStudent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SecondStudent"))
                .andExpect(jsonPath("$.group").value("AnotherTestGroup"));
    }

    @Test
    public void getStudentById() throws Exception {
        mockMvc.perform(get("/students/" + idFirstStudent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("FirstStudent"))
                .andExpect(jsonPath("$.group").value("TestGroup"));
    }

    @Test
    public void getUnrealStudentCheckException() throws Exception {
        mockMvc.perform(get("/students/777"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(" Студент с id 777 не найден"));
    }

    @Test
    public void getAllStudents() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void getStudentsByGroup() throws Exception {
        mockMvc.perform(get("/students/group/TestGroup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

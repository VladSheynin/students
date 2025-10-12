package vsh.students.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.dto.StudentDTO;
import vsh.students.exception.DuplicateStudentException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Student;
import vsh.students.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsServiceTest {

    @InjectMocks
    private StudentsService studentsService;
    @Mock
    private StudentRepository studentRepository;

    private long studentId;
    private String studentName;
    private String groupName;

    @BeforeEach
    void setUp() {
        studentId = 1L;
        studentName = "test";
        groupName = "groupName";
    }

    @Test
    void addStudent_ShouldReturnExceptionIfStudentExist() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName(studentName);
        studentDTO.setGroup(groupName);

        when(studentRepository.findByName(studentName)).thenReturn(Optional.of(new Student()));
        DuplicateStudentException exception = assertThrows(DuplicateStudentException.class, () -> studentsService.addStudent(studentDTO));
        assertEquals("Такой студент уже есть", exception.getMessage());

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void addStudent_ShouldSaveAndReturnStudent() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setName(studentName);
        studentDTO.setGroup(groupName);

        Student student = new Student();
        student.setName(studentName);
        student.setGroup(groupName);
        student.setId(studentId);

        when(studentRepository.save(any(Student.class))).thenReturn(student);
        Student result = studentsService.addStudent(studentDTO);

        assertEquals(studentName, result.getName());
        assertEquals(groupName, result.getGroup());

        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void getStudentById_ShouldThrowStudentNotFoundException() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> studentsService.getStudentById(studentId));
        assertEquals(" Студент с id 1 не найден", exception.getMessage());

        verify(studentRepository, times(1)).findById(any());
    }

    @Test
    void getStudentById_ShouldReturnStudent() {
        Student student = new Student();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Student result = studentsService.getStudentById(studentId);

        assertNotNull(result);
        assertEquals(student, result);

        verify(studentRepository, times(1)).findById(any());
    }

    @Test
    void getStudentByName_ShouldThrowStudentNotFoundException() {
        when(studentRepository.findByName(studentName)).thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> studentsService.getStudentByName(studentName));
        assertEquals("Студент с именем 'test' не найден", exception.getMessage());

        verify(studentRepository, times(1)).findByName(any());
    }

    @Test
    void getStudentByName_ShouldReturnStudent() {
        Student student = new Student();

        when(studentRepository.findByName(studentName)).thenReturn(Optional.of(student));

        Student result = studentsService.getStudentByName(studentName);

        assertNotNull(result);
        assertEquals(student, result);

        verify(studentRepository, times(1)).findByName(any());
    }

    @Test
    void getAllStudents_ShouldReturnSizeListOfStudents() {
        Student student = new Student();
        when(studentRepository.findAll()).thenReturn(List.of(student, student));

        List<Student> result = studentsService.getAllStudents();
        assertEquals(2, result.size());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void getStudentsByGroup_ShouldReturnSizeListOfStudent() {
        Student student = new Student();
        when(studentRepository.findByGroup(groupName)).thenReturn(List.of(student));

        List<Student> result = studentsService.getStudentsByGroup(groupName);
        assertEquals(1, result.size());

        verify(studentRepository, times(1)).findByGroup(any());
    }

    @Test
    void getStudentsByIds_ShouldReturnSizeListOfStudents() {
        Student student1 = new Student();
        Student student2 = new Student();
        when(studentRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(student1, student2));

        List<Student> result = studentsService.getStudentsByIds(List.of(1L, 2L));
        assertEquals(2, result.size());

        verify(studentRepository, times(1)).findAllByIdIn(any());
    }

    @Test
    void existsById_ShouldReturnTrueIfStudentExist() {
        Student student = new Student();
        when(studentRepository.existsById(studentId)).thenReturn(true);
        assertTrue(studentsService.existsById(studentId));

        verify(studentRepository, times(1)).existsById(studentId);
    }

    @Test
    void existsById_ShouldReturnFalseIfStudentNotExist() {
        Student student = new Student();
        when(studentRepository.existsById(studentId)).thenReturn(false);
        assertFalse(studentsService.existsById(studentId));

        verify(studentRepository, times(1)).existsById(studentId);
    }
}
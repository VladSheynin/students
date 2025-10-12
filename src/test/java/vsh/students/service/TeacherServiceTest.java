package vsh.students.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.dto.TeacherDTO;
import vsh.students.exception.DuplicateTeacherException;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Teacher;
import vsh.students.repositories.TeacherRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepository;
    @InjectMocks
    TeacherService teacherService;

    private String teacherName;
    private String teacherDepartment;
    private long teacherId;

    @BeforeEach
    void setUp() {
        teacherId = 1L;
        teacherName = "TeacherName";
        teacherDepartment = "Department";
    }

    @Test
    void addTeacher_ShouldSaveAndReturnTeacher() {
        Teacher teacher = new Teacher();
        teacher.setName(teacherName);
        teacher.setName(teacherDepartment);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setName(teacherName);
        teacherDTO.setDepartment(teacherDepartment);

        Teacher result = teacherService.addTeacher(teacherDTO);
        assertEquals(teacherName, result.getName());
        assertEquals(teacherDepartment, result.getDepartment());

        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void addTeacher_ShouldReturnExceptionIfTeacherExist() {
        Teacher teacher = new Teacher();
        teacher.setName(teacherName);
        teacher.setDepartment(teacherDepartment);
        when(teacherRepository.findByName(teacherName)).thenReturn(Optional.of(teacher));

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setName(teacherName);
        teacherDTO.setDepartment(teacherDepartment);

        DuplicateTeacherException exception = assertThrows(DuplicateTeacherException.class, () -> teacherService.addTeacher(teacherDTO));
        assertEquals("Такой преподаватель уже есть", exception.getMessage());
    }

    @Test
    void getTeacherById_ShouldThrowTeacherNotFoundException() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        TeacherNotFoundException exception = assertThrows(TeacherNotFoundException.class, () -> teacherService.getTeacherById(teacherId));
        assertEquals("Преподаватель с id 1 не найден", exception.getMessage());

        verify(teacherRepository, times(1)).findById(any());
    }

    @Test
    void getTeacherById_ShouldReturnTeacher() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        Teacher result = teacherService.getTeacherById(teacherId);

        assertNotNull(result);
        assertEquals(teacher, result);

        verify(teacherRepository, times(1)).findById(any());
    }

    @Test
    void getTeacherByName_ShouldThrowTeacherNotFoundException() {
        when(teacherRepository.findByName(teacherName)).thenReturn(Optional.empty());

        TeacherNotFoundException exception = assertThrows(TeacherNotFoundException.class, () -> teacherService.getTeacherByName(teacherName));
        assertEquals("Преподаватель с именем 'TeacherName' не найден", exception.getMessage());

        verify(teacherRepository, times(1)).findByName(any());
    }

    @Test
    void getTeacherByName_ShouldReturnTeacher() {
        Teacher teacher = new Teacher();

        when(teacherRepository.findByName(teacherName)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherByName(teacherName);

        assertNotNull(result);
        assertEquals(teacher, result);

        verify(teacherRepository, times(1)).findByName(any());
    }

    @Test
    void getAllTeachers_ShouldReturnSizeListOfTeachers() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findAll()).thenReturn(List.of(teacher, teacher));

        List<Teacher> result = teacherService.getAllTeachers();
        assertEquals(2, result.size());

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void getTeachersByGroup_ShouldReturnSizeListOfTeacher() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findByDepartment(teacherDepartment)).thenReturn(List.of(teacher));

        List<Teacher> result = teacherService.getTeachersByDepartment(teacherDepartment);
        assertEquals(1, result.size());

        verify(teacherRepository, times(1)).findByDepartment(any());
    }

    @Test
    void existsById_ShouldReturnFalseIfTeacherNotExist() {
        Teacher teacher = new Teacher();
        when(teacherRepository.existsById(teacherId)).thenReturn(false);
        assertFalse(teacherService.existsById(teacherId));

        verify(teacherRepository, times(1)).existsById(teacherId);
    }
}
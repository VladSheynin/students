package vsh.students.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.dto.AttendanceDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.NoAttendanceException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Attendance;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.repositories.AttendanceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {
    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentsService studentsService;

    @InjectMocks
    private AttendanceService attendanceService;

    private long studentId;
    private long courseId;

    @BeforeEach
    void setUp() {
        studentId = 1L;
        courseId = 2L;
    }

    @Test
    void addAttendance_ShouldSaveAndReturnAttendance() {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setStudent_id(studentId);
        dto.setCourse_id(courseId);
        dto.setDate(LocalDate.now());
        dto.setPresent(true);

        Student student = new Student();
        Course course = new Course();

        when(studentsService.getStudentById(dto.getStudent_id())).thenReturn(student);
        when(courseService.getCourseById(dto.getCourse_id())).thenReturn(course);

        doAnswer(invocation -> {
            Attendance attendanceArg = invocation.getArgument(0);
            assertEquals(student, attendanceArg.getStudent());
            assertEquals(course, attendanceArg.getCourse());
            assertEquals(LocalDate.now(), attendanceArg.getDate());
            assertTrue(attendanceArg.isPresent());
            return null;
        }).when(attendanceRepository).save(any(Attendance.class));

        Attendance result = attendanceService.addAttendance(dto);

        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
        assertEquals(LocalDate.now(), result.getDate());
        assertTrue(result.isPresent());

        verify(studentsService, times(1)).getStudentById(dto.getStudent_id());
        verify(courseService, times(1)).getCourseById(dto.getCourse_id());
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void getAttendanceByStudentId_ShouldThrowStudentNotFoundException() {
        when(studentsService.existsById(studentId)).thenReturn(false);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> attendanceService.getAttendanceByStudentId(studentId));
        assertEquals("Студент с ID 1 не найден", exception.getMessage());

        verify(studentsService, times(1)).existsById(studentId);
        verifyNoInteractions(attendanceRepository);
    }

    @Test
    void getAttendanceByStudentId_ShouldReturnAttendanceList() {
        List<Attendance> attendanceList = new ArrayList<>();
        attendanceList.add(new Attendance());
        attendanceList.add(new Attendance());

        when(studentsService.existsById(studentId)).thenReturn(true);
        when(attendanceRepository.findByStudent_Id(studentId)).thenReturn(attendanceList);

        List<Attendance> result = attendanceService.getAttendanceByStudentId(studentId);

        assertEquals(2, result.size());

        verify(studentsService, times(1)).existsById(studentId);
        verify(attendanceRepository, times(1)).findByStudent_Id(studentId);
    }

    @Test
    void getStudentAttendanceByCourse_ShouldThrowStudentNotFound() {
        when(studentsService.existsById(studentId)).thenReturn(false);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> attendanceService.getStudentAttendanceByCourse(studentId, courseId));
        assertEquals(" Студент с id 1 не найден", exception.getMessage());

        verify(studentsService, times(1)).existsById(studentId);
        verifyNoMoreInteractions(courseService, attendanceRepository, studentsService);
    }

    @Test
    void getStudentAttendanceByCourse_ShouldThrowCourseNotFound() {
        when(studentsService.existsById(studentId)).thenReturn(true);
        when(courseService.existsById(courseId)).thenReturn(false);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> attendanceService.getStudentAttendanceByCourse(studentId, courseId));
        assertEquals("Курс с ID 2 не найден", exception.getMessage());

        verify(studentsService, times(1)).existsById(studentId);
        verify(courseService, times(1)).existsById(courseId);
        verifyNoMoreInteractions(attendanceRepository, studentsService);
    }

    @Test
    void getStudentAttendanceByCourse_ShouldThrowNoAttendanceException() {
        when(studentsService.existsById(studentId)).thenReturn(true);
        when(courseService.existsById(courseId)).thenReturn(true);
        Student student = new Student();
        Course course = new Course();
        when(studentsService.getStudentById(studentId)).thenReturn(student);
        when(courseService.getCourseById(courseId)).thenReturn(course);
        when(attendanceRepository.getStudentAttendance(student, course)).thenReturn(null);

        NoAttendanceException exception = assertThrows(NoAttendanceException.class, () -> attendanceService.getStudentAttendanceByCourse(studentId, courseId));
        assertEquals("Студент не посещал данный курс (нет фактов посещения/пропуска)", exception.getMessage());

        verify(studentsService, times(1)).existsById(studentId);
        verify(courseService, times(1)).existsById(courseId);
        verify(studentsService, times(1)).getStudentById(studentId);
        verify(courseService, times(1)).getCourseById(courseId);
        verify(attendanceRepository, times(1)).getStudentAttendance(student, course);
    }

    @Test
    void getStudentAttendanceByCourse_ShouldReturnAttendanceDTO() {
        when(studentsService.existsById(studentId)).thenReturn(true);
        when(courseService.existsById(courseId)).thenReturn(true);

        Student student = new Student();
        Course course = new Course();
        StudentAttendanceCountDTO dto = new StudentAttendanceCountDTO(student, 2L, 2L);
        when(studentsService.getStudentById(studentId)).thenReturn(student);
        when(courseService.getCourseById(courseId)).thenReturn(course);
        when(attendanceRepository.getStudentAttendance(student, course)).thenReturn(dto);

        StudentAttendanceCountDTO result = attendanceService.getStudentAttendanceByCourse(studentId, courseId);

        assertEquals(dto, result);

        verify(studentsService, times(1)).existsById(studentId);
        verify(courseService, times(1)).existsById(courseId);
        verify(studentsService, times(1)).getStudentById(studentId);
        verify(courseService, times(1)).getCourseById(courseId);
        verify(attendanceRepository, times(1)).getStudentAttendance(student, course);
    }
}
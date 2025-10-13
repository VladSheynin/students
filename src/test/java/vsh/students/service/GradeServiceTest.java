package vsh.students.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.dto.GradeDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.exception.*;
import vsh.students.model.Course;
import vsh.students.model.Grade;
import vsh.students.model.Student;
import vsh.students.repositories.GradeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    AttendanceService attendanceService;
    @Mock
    StudentsService studentsService;
    @Mock
    CourseService courseService;
    @Mock
    GradeRepository gradeRepository;
    @InjectMocks
    GradeService gradeService;

    List<Grade> gradeList;
    long courseId;
    Course course;
    Student student;

    @BeforeEach
    void setUp() {
        courseId = 1L;
        student = new Student();
        student.setName("TestStudent");
        student.setId(10L);
        course = new Course();
        course.setName("TestCourse");
        course.setId(courseId);

        gradeList = new ArrayList<>();
        gradeList.add(new Grade());
        gradeList.add(new Grade());
        gradeList.get(0).setGrade(5);
        gradeList.get(0).setGradedAt(LocalDate.now());
        gradeList.get(0).setCourse(course);
        gradeList.get(0).setStudent(student);
        gradeList.get(1).setGrade(4);
        gradeList.get(1).setGradedAt(LocalDate.now().minusDays(1));
    }

    @Test
    void addGrade_ShouldReturnGradeIfStudentHasGradeForThisCourseAndHisAttendanceMoreThanPERCENT_FOR_ATTENDANCE() {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setGradeSize(5);
        gradeDTO.setGradeAt(LocalDate.now());
        gradeDTO.setStudent_id(student.getId());
        gradeDTO.setCourse_id(course.getId());

        when(studentsService.getStudentById(gradeDTO.getStudent_id())).thenReturn(student);
        when(studentsService.existsById(gradeDTO.getStudent_id())).thenReturn(true);
        when(courseService.existsById(gradeDTO.getCourse_id())).thenReturn(true);
        when(courseService.getCourseById(gradeDTO.getCourse_id())).thenReturn(course);

        StudentAttendanceCountDTO studentAttendanceCountDTO = new StudentAttendanceCountDTO(student, 8, 4);
        when(attendanceService.getStudentAttendanceByCourse(student.getId(), course.getId())).thenReturn(studentAttendanceCountDTO);

        Grade result = gradeService.addGrade(gradeDTO);

        assertEquals(5, result.getGrade());
        assertEquals(LocalDate.now(), result.getGradedAt());
        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());

        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void addGrade_ShouldExceptionIfStudentHasGradeByThisCourse() {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setGradeSize(5);
        gradeDTO.setGradeAt(LocalDate.now());
        gradeDTO.setStudent_id(student.getId());
        gradeDTO.setCourse_id(course.getId());
        when(studentsService.getStudentById(gradeDTO.getStudent_id())).thenReturn(student);
        when(studentsService.existsById(gradeDTO.getStudent_id())).thenReturn(true);
        when(courseService.existsById(gradeDTO.getCourse_id())).thenReturn(true);
        when(gradeRepository.findByStudent_idAndCourse_Id(student.getId(), courseId)).thenReturn(Optional.of(List.of(new Grade())));

        DuplicateGradeOnCourseException exception = assertThrows(DuplicateGradeOnCourseException.class, () -> gradeService.addGrade(gradeDTO));
        assertEquals("У данного студента уже есть оценка за этот курс ", exception.getMessage());

        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void addGrade_ShouldExceptionIfStudentHasPresentEqual0AndAbsencesEqual0() {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setGradeSize(5);
        gradeDTO.setGradeAt(LocalDate.now());
        gradeDTO.setStudent_id(student.getId());
        gradeDTO.setCourse_id(course.getId());

        when(studentsService.getStudentById(gradeDTO.getStudent_id())).thenReturn(student);
        when(studentsService.existsById(gradeDTO.getStudent_id())).thenReturn(true);
        when(courseService.existsById(gradeDTO.getCourse_id())).thenReturn(true);
        when(courseService.getCourseById(gradeDTO.getCourse_id())).thenReturn(course);

        StudentAttendanceCountDTO studentAttendanceCountDTO = new StudentAttendanceCountDTO(student, 0, 0);
        when(attendanceService.getStudentAttendanceByCourse(student.getId(), course.getId())).thenReturn(studentAttendanceCountDTO);

        NoAttendanceException exception = assertThrows(NoAttendanceException.class, () -> gradeService.addGrade(gradeDTO));
        assertEquals("Студент не посещал курс TestCourse", exception.getMessage());

        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void addGrade_ShouldExceptionIfStudentHasAttendanceLessThanPERCENT_FOR_ATTENDANCE() {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setGradeSize(5);
        gradeDTO.setGradeAt(LocalDate.now());
        gradeDTO.setStudent_id(student.getId());
        gradeDTO.setCourse_id(course.getId());

        when(studentsService.getStudentById(gradeDTO.getStudent_id())).thenReturn(student);
        when(studentsService.existsById(gradeDTO.getStudent_id())).thenReturn(true);
        when(courseService.existsById(gradeDTO.getCourse_id())).thenReturn(true);
        when(courseService.getCourseById(gradeDTO.getCourse_id())).thenReturn(course);

        StudentAttendanceCountDTO studentAttendanceCountDTO = new StudentAttendanceCountDTO(student, 5, 5);
        when(attendanceService.getStudentAttendanceByCourse(student.getId(), course.getId())).thenReturn(studentAttendanceCountDTO);

        LowAttendanceException exception = assertThrows(LowAttendanceException.class, () -> gradeService.addGrade(gradeDTO));
        assertEquals("Студент TestStudent на курсе TestCourse посетил всего 5 занятий из 10 (это 50.0%), при минимуме 60.0%", exception.getMessage());

        verify(gradeRepository, never()).save(any(Grade.class));
    }

    @Test
    void getGradeByStudentId_ShouldReturnListOfGradeIfStudentFound() {
        when(studentsService.existsById(student.getId())).thenReturn(true);
        when(gradeRepository.findByStudent_Id(student.getId())).thenReturn(Optional.of(gradeList));

        List<Grade> result = gradeService.getGradeByStudentId(student.getId());

        assertEquals(2, result.size());
        assertEquals(course, result.get(0).getCourse());
        assertEquals(student, result.get(0).getStudent());
        assertEquals(5, result.get(0).getGrade());
        assertEquals(LocalDate.now(), result.get(0).getGradedAt());
        assertEquals(4, result.get(1).getGrade());
        assertEquals(LocalDate.now().minusDays(1), result.get(1).getGradedAt());

        verify(gradeRepository, times(1)).findByStudent_Id(student.getId());
    }

    @Test
    void getGradeByStudentId_ShouldExceptionIfStudentNotFound() {
        when(studentsService.existsById(student.getId())).thenReturn(false);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> gradeService.getGradeByStudentId(student.getId()));
        assertEquals("Студент с ID 10 не найден", exception.getMessage());
    }

    @Test
    void getGradeByStudentIdAndCourse_ShouldReturnListOfGradeIfStudentAndCourseFound() {
        when(studentsService.existsById(student.getId())).thenReturn(true);
        when(courseService.existsById(course.getId())).thenReturn(true);
        when(gradeRepository.findByStudent_idAndCourse_Id(student.getId(), course.getId())).thenReturn(Optional.of(gradeList));

        List<Grade> result = gradeService.getGradeByStudentIdAndCourse(student.getId(), course.getId());

        assertEquals(2, result.size());
        assertEquals(course, result.get(0).getCourse());
        assertEquals(student, result.get(0).getStudent());
        assertEquals(5, result.get(0).getGrade());
        assertEquals(LocalDate.now(), result.get(0).getGradedAt());
        assertEquals(4, result.get(1).getGrade());
        assertEquals(LocalDate.now().minusDays(1), result.get(1).getGradedAt());

        verify(gradeRepository, times(1)).findByStudent_idAndCourse_Id(student.getId(), course.getId());
    }

    @Test
    void getGradeByStudentIdAndCourse_ShouldExceptionIfStudentNotFound() {
        when(studentsService.existsById(student.getId())).thenReturn(false);
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> gradeService.getGradeByStudentIdAndCourse(student.getId(), course.getId()));
        assertEquals("Студент с ID 10 не найден", exception.getMessage());

        verify(gradeRepository, never()).findByStudent_idAndCourse_Id(student.getId(), course.getId());
    }

    @Test
    void getGradeByStudentIdAndCourse_ShouldExceptionIfStudentFoundButCourseNotFound() {
        when(studentsService.existsById(student.getId())).thenReturn(true);
        when(courseService.existsById(course.getId())).thenReturn(false);
        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> gradeService.getGradeByStudentIdAndCourse(student.getId(), course.getId()));
        assertEquals("Курс с ID 1 не найден", exception.getMessage());

        verify(gradeRepository, never()).findByStudent_idAndCourse_Id(student.getId(), course.getId());
    }


    @Test
    void getGradeByCourseId_ShouldReturnListOfGradeIfCourseExist() {
        when(courseService.existsById(courseId)).thenReturn(true);
        when(gradeRepository.findByCourse_Id(1L)).thenReturn(Optional.of(gradeList));

        List<Grade> result = gradeService.getGradeByCourseId(courseId);

        assertEquals(2, result.size());
        assertEquals(course, result.get(0).getCourse());
        assertEquals(student, result.get(0).getStudent());
        assertEquals(5, result.get(0).getGrade());
        assertEquals(LocalDate.now(), result.get(0).getGradedAt());
        assertEquals(4, result.get(1).getGrade());
        assertEquals(LocalDate.now().minusDays(1), result.get(1).getGradedAt());

        verify(gradeRepository, times(1)).findByCourse_Id(courseId);
    }

    @Test
    void getGradeByCourseId_ShouldExceptionIfCourseNotExist() {
        when(courseService.existsById(courseId)).thenReturn(false);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> gradeService.getGradeByCourseId(courseId));
        assertEquals("Курс с ID 1 не найден", exception.getMessage());

        verify(gradeRepository, never()).findByCourse_Id(courseId);
    }
}
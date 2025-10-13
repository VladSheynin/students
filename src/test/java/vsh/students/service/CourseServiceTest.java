package vsh.students.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vsh.students.dto.CourseDTO;
import vsh.students.dto.StudentAbsenceCountDTO;
import vsh.students.exception.CourseNotFoundException;
import vsh.students.exception.DuplicateCourseException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Course;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.repositories.AttendanceRepository;
import vsh.students.repositories.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    CourseRepository courseRepository;
    @Mock
    AttendanceRepository attendanceRepository;
    @Mock
    TeacherService teacherService;
    @Mock
    StudentsService studentsService;
    @InjectMocks
    CourseService courseService;

    private long courseId;
    private Course course;
    private String courseName;
    private Teacher teacher;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        courseId = 1L;
        courseName = "courseName";

        teacher = new Teacher();
        teacher.setName("TeacherName");
        teacher.setId(1L);

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        students = List.of(student1, student2);

        course = new Course();
        course.setId(1L);
        course.setName(courseName);
        course.setTeacher(teacher);
        course.setStudents(students);
    }

    @Test
    void addCourse_ShouldReturnExceptionIfCourseExist() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("CourseName");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsIds(List.of(10L, 20L));
        when(courseRepository.findByNameWithTeacherAndStudents(courseDTO.getCourseName())).thenReturn(new Course());

        DuplicateCourseException exception = assertThrows(DuplicateCourseException.class, () -> courseService.addCourse(courseDTO));
        assertEquals("Курс с именем 'CourseName' уже существует", exception.getMessage());

        verify(courseRepository, times(1)).findByNameWithTeacherAndStudents(courseDTO.getCourseName());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void addCourse_ShouldReturnExceptionIfStudentsNotFound() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName(courseName);
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsIds(List.of(1L, 20L));

        when(studentsService.getStudentsByIds(courseDTO.getStudentsIds())).thenReturn(List.of(students.get(0)));
        when(courseRepository.findByNameWithTeacherAndStudents(courseDTO.getCourseName())).thenReturn(null);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);
        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> courseService.addCourse(courseDTO));
        assertEquals("Не найдены студенты с id: [20]", exception.getMessage());

        verify(courseRepository,never()).save(any());
    }


    @Test
    void addCourse_ShouldSaveAndReturnCourse() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName(courseName);
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsIds(List.of(10L, 20L));

        when(courseRepository.findByNameWithTeacherAndStudents(courseDTO.getCourseName())).thenReturn(null);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);
        when(studentsService.getStudentsByIds(courseDTO.getStudentsIds())).thenReturn(students);

        when(courseRepository.save(any(Course.class))).thenReturn(course);
        Course result = courseService.addCourse(courseDTO);

        assertEquals(courseName, result.getName());
        assertEquals(teacher.getName(), result.getTeacher().getName());
        assertEquals(students.get(1).getName(), result.getStudents().get(1).getName());

        verify(courseRepository, times(1)).save(any());
    }


    @Test
    void getCourseById_ShouldReturnCourse() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(courseId);

        assertEquals(1L, result.getId());
        assertEquals(courseName, result.getName());
        assertEquals(teacher, result.getTeacher());
        assertEquals(students, result.getStudents());

        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    void getCourseById_ShouldThrowCourseNotFoundException() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
        assertEquals("Курс с ID 1 не найден", exception.getMessage());

        verify(courseRepository, times(1)).findById(any());
    }

    @Test
    void getCourseByName_ShouldReturnCourse() {
        when(courseRepository.findByNameWithTeacherAndStudents(courseName)).thenReturn(course);

        Course result = courseService.getCourseByName(courseName);

        assertEquals(1L, result.getId());
        assertEquals(courseName, result.getName());
        assertEquals(teacher, result.getTeacher());
        assertEquals(students, result.getStudents());

        verify(courseRepository, times(1)).findByNameWithTeacherAndStudents(courseName);
    }

    @Test
    void getCourseByName_ShouldThrowCourseNotFoundException() {
        when(courseRepository.findByNameWithTeacherAndStudents(courseName)).thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getCourseByName(courseName));
        assertEquals("Курс с названием 'courseName' не найден", exception.getMessage());

        verify(courseRepository, times(1)).findByNameWithTeacherAndStudents(courseName);
    }

    @Test
    void getCoursesByTeacherName_ShouldReturnCourse() {
        when(courseRepository.findByTeacher_Name(teacher.getName())).thenReturn(List.of(course, course));
        when(teacherService.getTeacherByName(teacher.getName())).thenReturn(teacher);

        List<Course> result = courseService.getCoursesByTeacherName(teacher.getName());
        assertEquals(2, result.size());

        verify(courseRepository, times(1)).findByTeacher_Name(teacher.getName());
    }

    @Test
    void getCoursesByTeacherName_ShouldThrowTeacherNotFoundException() {
        when(teacherService.getTeacherByName(teacher.getName())).thenReturn(null);

        TeacherNotFoundException exception = assertThrows(TeacherNotFoundException.class, () -> courseService.getCoursesByTeacherName(teacher.getName()));
        assertEquals("Преподаватель с именем 'TeacherName' не найден", exception.getMessage());

        verify(courseRepository, never()).findByTeacher_Name(teacher.getName());
    }

    @Test
    void getCoursesByTeacherId_ShouldReturnCourse() {
        when(courseRepository.findByTeacher_Id(teacher.getId())).thenReturn(List.of(course, course));
        when(teacherService.getTeacherById(teacher.getId())).thenReturn(teacher);

        List<Course> result = courseService.getCoursesByTeacherId(teacher.getId());
        assertEquals(2, result.size());

        verify(courseRepository, times(1)).findByTeacher_Id(teacher.getId());
    }

    @Test
    void getCoursesByTeacherId_ShouldThrowTeacherNotFoundException() {
        when(teacherService.getTeacherById(teacher.getId())).thenReturn(null);

        TeacherNotFoundException exception = assertThrows(TeacherNotFoundException.class, () -> courseService.getCoursesByTeacherId(teacher.getId()));
        assertEquals("Преподаватель с id '1' не найден", exception.getMessage());

        verify(courseRepository, never()).findByTeacher_Id(teacher.getId());
    }

    @Test
    void addCourse_ShouldReturnSizeListOfCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(course, course, course));

        List<Course> result = courseService.getAllCourses();
        assertEquals(3, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getAverageGradeByCourse_ShouldReturnAverageGradeByCourses() {
        when(courseRepository.existsById(course.getId())).thenReturn(true);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(courseRepository.findAverageGradeByCourse(course)).thenReturn(4D);

        Double averageGrade = courseService.getAverageGradeByCourse(course.getId());

        assertEquals(4D, averageGrade);

        verify(courseRepository, times(1)).existsById(course.getId());
        verify(courseRepository, times(1)).findById(course.getId());
        verify(courseRepository, times(1)).findAverageGradeByCourse(course);
    }

    @Test
    void getAverageGradeByCourse_ShouldThrowCourseNotFoundException() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getAverageGradeByCourse(course.getId()));
        assertEquals("Курс с ID 1 не найден", exception.getMessage());

        verify(courseRepository, never()).findAverageGradeByCourse(any());
    }

    @Test
    void getPresentsByCourse_ShouldReturnListStudentWithAbsence() {
        when(courseRepository.existsById(course.getId())).thenReturn(true);
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        Student student = new Student();
        student.setName("FirstName");
        student.setGroup("FirstGroup");
        StudentAbsenceCountDTO absenceCountDTO = new StudentAbsenceCountDTO(student, 2);
        List<StudentAbsenceCountDTO> studentAbsenceCountDTOList = new ArrayList<>();
        studentAbsenceCountDTOList.add(absenceCountDTO);
        when(attendanceRepository.findStudentsWithAbsenceCount(any())).thenReturn(studentAbsenceCountDTOList);

        assertEquals(2, courseService.getPresentsByCourse(courseId).get(0).getAbsenceCount());

        verify(courseRepository, times(1)).existsById(course.getId());
        verify(courseRepository, times(1)).findById(course.getId());
        verify(attendanceRepository, times(1)).findStudentsWithAbsenceCount(course);
    }

    @Test
    void getPresentsByCourse_ShouldThrowCourseNotFoundException() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getPresentsByCourse(course.getId()));
        assertEquals("Курс с id 1 не существует", exception.getMessage());

        verify(attendanceRepository, never()).findStudentsWithAbsenceCount(course);
    }


    @Test
    void existsById_ShouldReturnFalseIfCourseNotExist() {
        when(courseRepository.existsById(courseId)).thenReturn(false);
        assertFalse(courseRepository.existsById(courseId));

        verify(courseRepository, times(1)).existsById(courseId);
    }
}
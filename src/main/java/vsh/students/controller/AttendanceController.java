package vsh.students.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.AttendanceDTO;
import vsh.students.dto.StudentAttendanceCountDTO;
import vsh.students.model.Attendance;
import vsh.students.service.AttendanceService;

import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<Attendance> addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        Attendance saved = attendanceService.addAttendance(attendanceDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/student/{student_id}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentId(@PathVariable long student_id) {
        return ResponseEntity.ok().body(attendanceService.getAttendanceByStudentId(student_id));
    }

    @GetMapping(value = "/student/course/{student_id}/{course_id}")
    public ResponseEntity<StudentAttendanceCountDTO> getStudentAttendanceByCourseIdAndStudentId(@PathVariable long student_id, @PathVariable long course_id) {
        return ResponseEntity.ok().body(attendanceService.getStudentAttendanceByCourse(student_id, course_id));
    }

}

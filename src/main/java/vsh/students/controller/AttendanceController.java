package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.model.Attendance;
import vsh.students.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAttendance(@RequestParam long student_id, @RequestParam long course_id, @RequestParam LocalDate date, @RequestParam boolean present) {
        try {
            attendanceService.addAttendance(student_id, course_id, date, present);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(value = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Attendance>> getAttendanceByStudentId(@RequestParam long student_id) {
        return ResponseEntity.status(HttpStatus.OK).body(attendanceService.getAttendanceByStudentId(student_id));
    }

}

package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vsh.students.model.Attendance;
import vsh.students.service.AttendanceHelper;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AttendanceController {
    @Autowired
    private AttendanceHelper attendanceHelper;

    @PostMapping(value = "/attendance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addAttendance(@RequestParam long student_id, @RequestParam long course_id, @RequestParam LocalDate date, @RequestParam boolean present) {
        try {
            attendanceHelper.addAttendance(student_id, course_id, date, present);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(value = "/attendance/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Attendance>> getAttendanceByStudentId(@RequestParam long student_id) {
        return ResponseEntity.status(HttpStatus.OK).body(attendanceHelper.getGradeByStudentId(student_id));
    }

}

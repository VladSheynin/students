package vsh.students.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vsh.students.dto.AttendanceDTO;
import vsh.students.model.Attendance;
import vsh.students.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Attendance> addAttendance(@RequestBody AttendanceDTO attendanceDTO) {
            Attendance saved = attendanceService.addAttendance(attendanceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/student/{student_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Attendance>> getAttendanceByStudentId(@PathVariable long student_id) {
        return ResponseEntity.status(HttpStatus.OK).body(attendanceService.getAttendanceByStudentId(student_id));
    }

}

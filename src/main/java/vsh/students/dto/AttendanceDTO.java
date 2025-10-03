package vsh.students.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AttendanceDTO {
    private long student_id;
    private long course_id;
    private LocalDate date;
    private boolean present;
}

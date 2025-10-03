package vsh.students.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class GradeDTO {
    private long student_id;
    private long course_id;
    private int gradeSize;
    private LocalDate gradeAt;
}

package vsh.students.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vsh.students.model.Student;

@Getter
@Setter
@RequiredArgsConstructor
public class StudentAbsenceCountDTO {
    private Student student;
    private long absenceCount;

}

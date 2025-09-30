package vsh.students.dto;

import vsh.students.model.Student;

public class StudentAttendanceCountDTO {
    private final Student student;
    private final long present;
    private final long absences;

    public StudentAttendanceCountDTO(Student student, long present, long absences) {
        this.student = student;
        this.present = present;
        this.absences = absences;
    }

    public Student getStudent() {
        return student;
    }

    public long getPresent() {
        return present;
    }

    public long getAbsences() {
        return absences;
    }
}

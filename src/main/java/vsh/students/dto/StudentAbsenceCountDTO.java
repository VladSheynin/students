package vsh.students.dto;

import vsh.students.model.Student;

public record StudentAbsenceCountDTO(Student student, long absenceCount) {
}

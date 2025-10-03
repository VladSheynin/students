package vsh.students.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CourseDTO {
    private String courseName;      // Название курса
    private long teacherId;         // ID преподавателя
    private List<Long> studentsIds; // Список ID студентов
}

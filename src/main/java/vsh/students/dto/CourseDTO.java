package vsh.students.dto;

import java.util.List;

public class CourseDTO {
    private String courseName;      // Название курса
    private long teacherId;         // ID преподавателя
    private List<Long> studentsIds; // Список ID студентов

    public CourseDTO() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public List<Long> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(List<Long> studentsIds) {
        this.studentsIds = studentsIds;
    }
}

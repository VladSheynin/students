package vsh.students.dto;

import java.time.LocalDate;

public class GradeDTO {

    private long student_id;
    private long course_id;
    private int gradeSize;
    private LocalDate gradeAt;

    public GradeDTO() {
    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(long course_id) {
        this.course_id = course_id;
    }

    public int getGradeSize() {
        return gradeSize;
    }

    public void setGradeSize(int gradeSize) {
        this.gradeSize = gradeSize;
    }

    public LocalDate getGradeAt() {
        return gradeAt;
    }

    public void setGradeAt(LocalDate gradeAt) {
        this.gradeAt = gradeAt;
    }
}

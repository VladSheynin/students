package vsh.students.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "student_name")
    String name;

    @Column(name = "group_name")
    String group;

    public Student() {
    }
}

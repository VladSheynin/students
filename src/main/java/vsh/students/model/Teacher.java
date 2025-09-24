package vsh.students.model;

import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "teacher_name")
    String name;

    @Column(name = "department")
    String department;

    public Teacher() {
    }

}

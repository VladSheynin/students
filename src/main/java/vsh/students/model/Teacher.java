package vsh.students.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_name")
    private String name;

    @Column(name = "department")
    private String department;

    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();
}

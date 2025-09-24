package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.model.Student;
import vsh.students.reposiroies.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsHelper {
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Создать студента
     *
     * @param name  - фио студента
     * @param group -группа студента
     */
    public void addStudent(String name, String group) {
        if (getStudentByName(name) != null) throw new NonUniqueResultException("Такой студент уже есть");
        Student student = new Student();
        student.setName(name);
        student.setGroup(group);
        studentRepository.save(student);
    }

    /**
     * Найти студента по id
     *
     * @param id - идентификатор студента
     * @return - объект студент (optional)
     */
    public Student getStudentById(long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        List<Student> listStudents = optionalStudent.map(List::of).orElseGet(List::of);
        if (listStudents.isEmpty()) throw new EntityNotFoundException("Студент не найден");
        return listStudents.getFirst();
    }

    /**
     * Получить Student по имени
     *
     */
    public Student getStudentByName(String name) {
        return studentRepository.findByName(name);
    }

    /**
     * Получить всех студентов
     *
     * @return список всех студентов
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Вернуть список студентов по группе
     *
     * @param group - имя группы
     * @return - список студентов
     */
    public List<Student> getStudentsByGroup(String group) {
        return studentRepository.findByGroup(group);
    }

}

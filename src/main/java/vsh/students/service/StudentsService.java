package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.dto.StudentDTO;
import vsh.students.exception.DuplicateStudentException;
import vsh.students.exception.StudentNotFoundException;
import vsh.students.model.Student;
import vsh.students.repositories.StudentRepository;

import java.util.List;

@Service
public class StudentsService {
    @Autowired
    private StudentRepository studentRepository;

    /**
     * Создать студента
     *
     * @param studentDTO - DTO студента
     */
    public Student addStudent(StudentDTO studentDTO) {
        if (studentRepository.findByName(studentDTO.getName()).isPresent()) {
            throw new DuplicateStudentException("Такой студент уже есть");
        }

        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setGroup(studentDTO.getGroup());
        studentRepository.save(student);
        return student;
    }

    /**
     * Найти студента по id
     *
     * @param id - идентификатор студента
     * @return - объект студент
     */
    public Student getStudentById(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(" Студент с id " + id + " не найден"));
    }

    /**
     * Получить Student по имени
     *
     */
    public Student getStudentByName(String name) {
        return studentRepository.findByName(name).orElseThrow(() -> new StudentNotFoundException("Студент с именем '" + name + "' не найден"));
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

    /**
     * Список студентов по списку id
     *
     * @param ids - список идентификаторов студентов
     * @return - список студентов
     */
    public List<Student> getStudentsByIds(List<Long> ids) {
        return studentRepository.findAllByIdIn(ids);
    }

    /**
     * Проверка наличия студента с id
     *
     * @param id - конкретный id
     * @return - true|false если студент есть в базе
     */
    public boolean existsById(long id) {
        return studentRepository.existsById(id);
    }


}

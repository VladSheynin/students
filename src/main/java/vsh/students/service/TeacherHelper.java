package vsh.students.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.model.Student;
import vsh.students.model.Teacher;
import vsh.students.reposiroies.StudentRepository;
import vsh.students.reposiroies.TeacherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherHelper {
    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Создать преподавателя
     *
     * @param name  - фио преподавателя
     * @param department - департамент преподавателя
     */
    public void addTeacher(String name, String department) {
        if (getTeacherByName(name) != null) throw new NonUniqueResultException("Такой преподаватель уже есть");
        Teacher teacher = new Teacher();
        teacher.setName(name);
        teacher.setDepartment(department);
        teacherRepository.save(teacher);
    }

    /**
     * Найти студента по id
     *
     * @param id - идентификатор студента
     * @return - объект студент (optional)
     */
    public Teacher getTeacherById(long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        List<Teacher> listTeachers = optionalTeacher.map(List::of).orElseGet(List::of);
        if (listTeachers.isEmpty()) throw new EntityNotFoundException("Преподаватель не найден");
        return listTeachers.getFirst();
    }

    /**
     * Получить Student по имени
     *
     */
    public Teacher getTeacherByName(String name) {
        return teacherRepository.findByName(name);
    }

    /**
     * Получить всех студентов
     *
     * @return список всех студентов
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Вернуть список студентов по группе
     *
     * @param department - департамент
     * @return - список преподавателей
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

}

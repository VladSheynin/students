package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.dto.TeacherDTO;
import vsh.students.exception.DuplicateTeacherException;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Teacher;
import vsh.students.repositories.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Создать преподавателя
     *
     * @param teacherDTO - DTO преподавателя
     */
    public Teacher addTeacher(TeacherDTO teacherDTO) {
        if (teacherRepository.findByName(teacherDTO.getName()).isPresent()) {
            throw new DuplicateTeacherException("Такой преподаватель уже есть");
        }

        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.getName());
        teacher.setDepartment(teacherDTO.getDepartment());
        teacherRepository.save(teacher);
        return teacher;
    }

    /**
     * Найти студента по id
     *
     * @param id - идентификатор студента
     * @return - объект студент (optional)
     */
    public Teacher getTeacherById(long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFoundException("Преподаватель с id " + id + " не найден"));
    }

    /**
     * Получить Student по имени
     *
     */
    public Teacher getTeacherByName(String name) {
        return teacherRepository.findByName(name).orElseThrow(() -> new TeacherNotFoundException("Преподаватель с именем '" + name + "' не найден"));
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

    /**
     * Проверка наличия преподавателя с id
     *
     * @param id - конкретный id
     * @return - true|false если преподаватель есть в базе
     */
    public boolean existsById(long id) {
        return teacherRepository.existsById(id);
    }

}

package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vsh.students.dto.TeacherDTO;
import vsh.students.exception.DuplicateTeacherException;
import vsh.students.exception.TeacherNotFoundException;
import vsh.students.model.Teacher;
import vsh.students.repositories.TeacherRepository;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Создать преподавателя
     *
     * @param teacherDTO - DTO преподавателя
     */
    @Transactional
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
     * Найти преподавателя по id
     *
     * @param id - идентификатор преподавателя
     * @return - объект преподаватель
     */
    public Teacher getTeacherById(long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFoundException("Преподаватель с id " + id + " не найден"));
    }

    /**
     * Получить преподавателя по имени
     *
     */
    public Teacher getTeacherByName(String name) {
        return teacherRepository.findByName(name).orElseThrow(() -> new TeacherNotFoundException("Преподаватель с именем '" + name + "' не найден"));
    }

    /**
     * Получить всех преподавателей
     *
     * @return список всех преподавателей
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Вернуть список преподавателей по департаменту
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

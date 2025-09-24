package vsh.students.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vsh.students.model.Course;
import vsh.students.model.Teacher;
import vsh.students.reposiroies.CourseRepository;

import java.util.List;

@Service
public class CourseHelper {
    @Autowired
    private CourseRepository courseRepository;

    public void addCourse(String course_name, String course_teacher,String students)
    {

    }

    public List<Course> getCoursesByTeacherName(String name){
        return courseRepository.findByTeacher_Name(name);
    }

}

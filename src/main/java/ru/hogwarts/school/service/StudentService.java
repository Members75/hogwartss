package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private Map<Long, Student> students = new HashMap<>();
    private Long idCounter = 1L;

    public Student create(Student student) {
        student.setId(idCounter++);
        students.put(student.getId(), student);
        return student;
    }

    public Student get(Long id) {
        return students.get(id);
    }

    public Student update(Long id, Student updatedStudent) {
        if (students.containsKey(id)) {
            updatedStudent.setId(id);
            students.put(id, updatedStudent);
            return updatedStudent;
        }
        return null;
    }

    public void delete(Long id) {
        students.remove(id);
    }

    public Map<Long, Student> getAll() {
        return students;
    }
}

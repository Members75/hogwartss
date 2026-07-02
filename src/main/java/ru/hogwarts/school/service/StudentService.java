package ru.hogwarts.school.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAgeRange(int minAge, int maxAge) {
        if (minAge > maxAge) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minAge cannot be greater than maxAge");
        }
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Map<Long, Student> getAll() {
        return null;
    }

    public Student get(Long id) {
        return null;
    }

    public Student create(Student student) {
        return null;
    }

    public Student update(Long id, Student student) {
        return null;
    }

    public void delete(Long id) {

    }
}
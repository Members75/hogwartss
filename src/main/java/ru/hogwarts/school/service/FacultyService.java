package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public void deleteStudent(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        facultyRepository.deleteById(id);
    }


    public List<Faculty> getStudentsByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> getAll() {
        return null;
    }

    public Faculty getById(Long id) {
        return null;
    }

    public Faculty create(Faculty faculty) {
        return null;
    }

    public Faculty update(Long id, Faculty faculty) {
        return null;
    }

    public void delete(Long id) {
    }

    public List<Faculty> getByColor(String color) {
        return null;
    }
}
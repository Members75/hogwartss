package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class StreamController {

    private final StudentService studentService;
    private final FacultyService facultyService;

    public StreamController(StudentService studentService, FacultyService facultyService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
    }


    @GetMapping("/students/names-starting-with-a")
    public List<String> getStudentNamesStartingWithA() {
        return studentService.getAllStudents().stream()
                .map(Student::getName)                 // достаём имя
                .map(String::toUpperCase)              // сначала в верхний регистр
                .filter(name -> name.startsWith("A"))  // потом фильтр
                .sorted()                               // сортировка после фильтрации
                .collect(Collectors.toList());
    }


    @GetMapping("/students/average-age")
    public double getAverageAge() {
        return studentService.getAllStudents().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @GetMapping("/faculties/longest-name")
    public String getLongestFacultyName() {
        return facultyService.getAllFaculties().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    @GetMapping("/math/sum-optimized")
    public int getSumOptimized() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel().reduce(0, Integer::sum);
    }
}

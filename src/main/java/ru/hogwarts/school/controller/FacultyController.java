package ru.hogwarts.school.controller;

import org.apache.catalina.util.ParameterMap;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public Map<Long, Faculty> getAll() {
        return facultyService.getAll();
    }

    @GetMapping("/{id}")
    public Faculty getById(@PathVariable Long id) {
        return facultyService.get(id);
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable Long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facultyService.delete(id);
    }

    @GetMapping("/color/{color}")
    public Map<Long, Faculty> filterByColor(@PathVariable String color) {
        return facultyService.getAll().entrySet()
                .stream()
                .filter(e -> e.getValue().getColor().equalsIgnoreCase(color))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
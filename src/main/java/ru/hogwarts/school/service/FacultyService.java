package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultys = new HashMap<>();
    private Long idCounter = 1L;

    public Faculty create(Faculty faculty) {
        facultys.get(idCounter++);
        facultys.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty get(Long id) {
        return facultys.get(id);
    }

    public Faculty update(Long id, Faculty updatedStudent) {
        if (facultys.containsKey(id)) {
            updatedStudent.setId(id);
            facultys.put(id, updatedStudent);
            return updatedStudent;
        }
        return null;
    }

    public void delete(Long id) {
        facultys.remove(id);
    }

    public Map<Long, Faculty> getAll() {
        return facultys;
    }
}

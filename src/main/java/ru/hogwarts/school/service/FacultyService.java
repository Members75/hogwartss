package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty saveFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        logger.debug("Creating faculty with name={}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Was invoked method for get faculty by id");
        logger.debug("Fetching faculty with id={}", id);

        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "There is no faculty with id = " + id;
                    logger.error(msg);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
                });
    }

    public Faculty getFacultyWithStudents(Long id) {
        logger.info("Was invoked method for get faculty with students");
        logger.debug("Fetching faculty with students, id={}", id);

        Faculty faculty = facultyRepository.findByIdWithStudents(id);
        if (faculty == null) {
            String msg = "There is no faculty with id = " + id + " (with students)";
            logger.error(msg);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        return faculty;
    }

    public List<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty");
        logger.debug("Deleting faculty with id={}", id);

        if (!facultyRepository.existsById(id)) {
            String msg = "There is no faculty with id = " + id + " to delete";
            logger.error(msg);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        facultyRepository.deleteById(id);
    }

    public List<Faculty> searchFaculties(String keyword) {
        logger.info("Was invoked method for search faculties");
        logger.debug("Searching faculties with keyword={}", keyword);
        return facultyRepository.findByNameContainingIgnoreCase(keyword);
    }
}
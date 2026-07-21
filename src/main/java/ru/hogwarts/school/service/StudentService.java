package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name={}, age={}", student.getName(), student.getAge());
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        logger.info("Was invoked method for get student by id");
        logger.debug("Fetching student with id={}", id);

        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "There is no student with id = " + id;
                    logger.error(msg);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
                });
    }

    public Student getStudentWithFaculty(Long id) {
        logger.info("Was invoked method for get student with faculty");
        logger.debug("Fetching student with faculty, id={}", id);

        Student student = studentRepository.findByIdWithFaculty(id);
        if (student == null) {
            String msg = "There is no student with id = " + id + " (with faculty)";
            logger.error(msg);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        return student;
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Deleting student with id={}", id);

        if (!studentRepository.existsById(id)) {
            String msg = "There is no student with id = " + id + " to delete";
            logger.error(msg);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByAgeRange(int minAge, int maxAge) {
        logger.info("Was invoked method for get students by age range");
        logger.debug("Searching students with age between {} and {}", minAge, maxAge);

        if (minAge > maxAge) {
            String msg = "Invalid age range: minAge (" + minAge + ") cannot be greater than maxAge (" + maxAge + ")";
            logger.warn(msg);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
        }
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public long getTotalStudentsCount() {
        logger.info("Was invoked method for get total students count");
        return studentRepository.countAllStudents();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age");
        Double avg = studentRepository.averageAge();
        double result = (avg == null) ? 0.0 : avg;
        logger.debug("Calculated average age={}", result);
        return result;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }
}
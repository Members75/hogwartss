package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s JOIN FETCH s.faculty WHERE s.id = :id")
    Student findByIdWithFaculty(@Param("id") Long id);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    @Query("SELECT COUNT(s) FROM Student s")
    long countAllStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double averageAge();

    @Query(value = "SELECT * FROM students ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();
}
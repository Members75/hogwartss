package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testCreateStudent_Success() {
        Student student = new Student();
        student.setName("Harry Potter");
        student.setAge(11);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                getUrl("/students"), student, Student.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Harry Potter", response.getBody().getName());
        assertEquals(11, response.getBody().getAge());
    }

    @Test
    void testGetAllStudents_Success() {
        // Сначала создадим пару студентов, чтобы было что проверять
        Student s1 = new Student(); s1.setName("Ron Weasley"); s1.setAge(12);
        Student s2 = new Student(); s2.setName("Hermione Granger"); s2.setAge(13);
        restTemplate.postForEntity(getUrl("/students"), s1, Student.class);
        restTemplate.postForEntity(getUrl("/students"), s2, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getUrl("/students"),
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
    }

    @Test
    void testGetStudentById_Success() {
        Student toCreate = new Student();
        toCreate.setName("Ron Weasley");
        toCreate.setAge(11);
        ResponseEntity<Student> createResp = restTemplate.postForEntity(
                getUrl("/students"), toCreate, Student.class);
        Long id = createResp.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getUrl("/students/" + id), Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ron Weasley", response.getBody().getName());
    }

    @Test
    void testGetStudentById_NotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getUrl("/students/999999"), Student.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateStudent_Success() {
        Student toCreate = new Student();
        toCreate.setName("Hermione Granger");
        toCreate.setAge(11);
        ResponseEntity<Student> createResp = restTemplate.postForEntity(
                getUrl("/students"), toCreate, Student.class);
        Long id = createResp.getBody().getId();

        Student student = new Student();
        student.setName("Hermione Granger Updated");
        student.setAge(12);

        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<Student> response = restTemplate.exchange(
                "/students/1",
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hermione Granger Updated", response.getBody().getName());
        assertEquals(12, response.getBody().getAge());
    }

    @Test
    void testDeleteStudent_Success() {
        Student toCreate = new Student();
        toCreate.setName("Neville Longbottom");
        toCreate.setAge(11);
        ResponseEntity<Student> createResp = restTemplate.postForEntity(
                getUrl("/students"), toCreate, Student.class);
        Long id = createResp.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange(
                getUrl("/students/" + id),
                org.springframework.http.HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Проверяем, что действительно удалилось
        ResponseEntity<Student> getAfter = restTemplate.getForEntity(
                getUrl("/students/" + id), Student.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfter.getStatusCode());
    }

    @Test
    void testGetStudentsByAgeRange_Filtering() {
        Student s1 = new Student(); s1.setName("Kid 10"); s1.setAge(10);
        Student s2 = new Student(); s2.setName("Kid 15"); s2.setAge(15);
        Student s3 = new Student(); s3.setName("Kid 20"); s3.setAge(20);

        restTemplate.postForEntity(getUrl("/students"), s1, Student.class);
        restTemplate.postForEntity(getUrl("/students"), s2, Student.class);
        restTemplate.postForEntity(getUrl("/students"), s3, Student.class);

        ResponseEntity<List<Student>> response = restTemplate.exchange(
                getUrl("/students/age-range?min=12&max=18"),
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Student>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Kid 15", response.getBody().get(0).getName());
    }
}
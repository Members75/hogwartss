package ru.hogwarts.school;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final List<Long> createdIds = new ArrayList<>();

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setUp() {
        createdIds.clear();
    }

    @AfterEach
    void tearDown() {
        for (Long id : createdIds) {
            try {
                ResponseEntity<Void> response = restTemplate.exchange(
                        getUrl("/faculties/" + id),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );
                if (response.getStatusCode() != HttpStatus.NO_CONTENT && response.getStatusCode() != HttpStatus.NOT_FOUND) {
                    throw new AssertionError("DELETE вернул неожиданный статус: " + response.getStatusCode());
                }
            } catch (AssertionError e) {
                throw e;
            } catch (Exception e) {
                // Логируем, но не ломаем тест, если ресурс уже удалён
            }
        }
    }

    @Test
    void testCreateFaculty_Success() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(getUrl("/faculties"), faculty, Faculty.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getId() != null && response.getBody().getId() > 0);
        assertEquals("Gryffindor", response.getBody().getName());
        assertEquals("Red", response.getBody().getColor());

        createdIds.add(response.getBody().getId());
    }

    @Test
    void testGetAllFaculties_Success() {
        Faculty f1 = new Faculty(); f1.setName("Slytherin"); f1.setColor("Green");
        Faculty f2 = new Faculty(); f2.setName("Ravenclaw"); f2.setColor("Blue");

        ResponseEntity<Faculty> r1 = restTemplate.postForEntity(getUrl("/faculties"), f1, Faculty.class);
        ResponseEntity<Faculty> r2 = restTemplate.postForEntity(getUrl("/faculties"), f2, Faculty.class);

        createdIds.add(r1.getBody().getId());
        createdIds.add(r2.getBody().getId());

        // ✅ Используем exchange + ParameterizedTypeReference<List<Faculty>>
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                getUrl("/faculties"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }
}
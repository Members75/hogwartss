package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private FacultyService facultyService;

    @BeforeEach
    void setup() {}

    @Test
    void testGetAllFaculties() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyById_Success() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Ravenclaw");
        faculty.setColor("Blue");

        when(facultyService.getFacultyById(eq(1L))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ravenclaw"));
    }

    @Test
    void testGetFacultyById_NotFound() throws Exception {
        when(facultyService.getFacultyById(eq(999L)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Faculty not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateFaculty() throws Exception {
        String payload = """
                {
                    "name": "Slytherin",
                    "color": "Green"
                }
                """;

        when(facultyService.saveFaculty(any(Faculty.class))).thenAnswer(inv -> {
            Faculty f = inv.getArgument(0);
            f.setId(2L); // эмуляция автоинкремента
            return f;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Slytherin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        // PUT /faculties/{id} в контроллере делает faculty.setId(id) и вызывает saveFaculty
        String payload = """
                {
                    "name": "Ravenclaw Updated",
                    "color": "Navy"
                }
                """;

        when(facultyService.saveFaculty(any(Faculty.class))).thenAnswer(inv -> {
            Faculty f = inv.getArgument(0);
            // id уже установлен контроллером, но можно убедиться
            return f;
        });

        mockMvc.perform(MockMvcRequestBuilders.put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ravenclaw Updated"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(eq(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculties/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testSearchFaculties_Filtering() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        when(facultyService.searchFaculties("Gryffindor")).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/search")
                        .param("q", "Gryffindor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyStudents() throws Exception {
        ru.hogwarts.school.model.Student student = new ru.hogwarts.school.model.Student();
        student.setName("Harry Potter");
        student.setAge(11);

        Faculty facultyWithStudents = new Faculty();
        facultyWithStudents.setId(1L);
        facultyWithStudents.setName("Test Faculty");
        facultyWithStudents.setColor("Orange");
        facultyWithStudents.setStudents(List.of(student));

        when(facultyService.getFacultyById(eq(1L))).thenReturn(facultyWithStudents);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Harry Potter"));
    }
}
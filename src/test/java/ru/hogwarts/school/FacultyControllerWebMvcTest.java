package ru.hogwarts.school;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;
    @MockitoBean
    private Faculty mockFaculty;

    @BeforeEach
    void setup() {
        mockFaculty = new Faculty();
        mockFaculty.setId(1L);
        mockFaculty.setName("Gryffindor");

        when(facultyService.getAllFaculties()).thenReturn(List.of(mockFaculty));
        when(facultyService.getFacultyById(anyLong())).thenReturn(mockFaculty);
        when(facultyService.saveFaculty(any(Faculty.class))).thenReturn(mockFaculty);
        doNothing().when(facultyService).deleteFaculty(anyLong());
        when(facultyService.searchFaculties(anyString())).thenReturn(List.of(mockFaculty));
    }

    @Test
    void testGetAllFaculties() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyById_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyById_NotFound() throws Exception {
        when(facultyService.getFacultyById(999L)).thenThrow(
                new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateFaculty() throws Exception {
        String payload = """
                {
                    "name": "Slytherin"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Slytherin"));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        String payload = """
                {
                    "name": "Ravenclaw Updated"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/faculties/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ravenclaw Updated"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/faculties/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testSearchFaculties_Filtering() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/search")
                        .param("q", "Gryffindor"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Gryffindor"));
    }

    @Test
    void testGetFacultyStudents() throws Exception {
        ru.hogwarts.school.model.Student student = new ru.hogwarts.school.model.Student();
        student.setName("Harry Potter");

        Faculty facultyWithStudents = new Faculty();
        facultyWithStudents.setId(1L);
        facultyWithStudents.setName("Test Faculty");
        facultyWithStudents.setStudents(List.of(student));

        when(facultyService.getFacultyById(1L)).thenReturn(facultyWithStudents);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties/1/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Harry Potter"));
    }
}

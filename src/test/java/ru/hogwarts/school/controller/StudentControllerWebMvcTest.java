package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private StudentService studentService;

    @BeforeEach
    void setup() {
    }

    @Test
    void testCreateStudent() throws Exception {
        String payload = """
                {
                    "name": "Harry Potter",
                    "age": 11
                }
                """;

        when(studentService.saveStudent(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Harry Potter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    void testGetStudentById_Success() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione Granger");
        student.setAge(12);

        when(studentService.getStudentById(eq(1L))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Hermione Granger"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(eq(999L)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Student not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/students/999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateStudent() throws Exception {
        String payload = """
                {
                    "name": "Ron Weasley Updated",
                    "age": 13
                }
                """;

        when(studentService.saveStudent(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            return s;
        });

        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ron Weasley Updated"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(eq(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testGetStudentsByAgeRange() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Neville Longbottom");
        student.setAge(14);

        when(studentService.getStudentsByAgeRange(10, 20)).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/students/age-range")
                        .param("min", "10")
                        .param("max", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Neville Longbottom"));
    }
}
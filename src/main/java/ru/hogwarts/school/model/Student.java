package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY лучше, чем EAGER, чтобы не тянуть лишние данные
    @JoinColumn(name = "faculty_id")
    @JsonIgnore // Важно: не сериализовать факультет при выводе студента
    private Faculty faculty;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Не сериализовать аватар при выводе студента (или наоборот — выбери сторону)
    private Avatar avatar;

    public Student() {}

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Геттеры и сеттеры (без изменений, кроме setFaculty)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Faculty getFaculty() { return faculty; }

    // ИСПРАВЛЕНО: принимает Faculty, возвращает void
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Avatar getAvatar() { return avatar; }
    public void setAvatar(Avatar avatar) { this.avatar = avatar; }
}
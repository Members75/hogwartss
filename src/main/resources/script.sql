DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS faculties CASCADE;

CREATE TABLE faculties
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE students
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    age        INTEGER,
    faculty_id BIGINT,
    CONSTRAINT fk_students_faculty
        FOREIGN KEY (faculty_id)
            REFERENCES faculties (id)
            ON DELETE SET NULL
);

INSERT INTO faculties (name)
VALUES ('Gryffindor'),
       ('Slytherin'),
       ('Ravenclaw'),
       ('Hufflepuff');


INSERT INTO students (name, age, faculty_id)
VALUES ('Harry Potter', 17, 1),
       ('Hermione Granger', 17, 1),
       ('Ron Weasley', 18, 1),
       ('Draco Malfoy', 18, 2),
       ('Luna Lovegood', 16, 3),
       ('Cedric Diggory', 19, 4),
       ('Neville Longbottom', 17, 1),
       ('Pansy Parkinson', 18, 2);

SELECT *
FROM students
WHERE age BETWEEN 10 AND 20;

SELECT name
FROM students;

SELECT *
FROM students
WHERE name ILIKE '%о%';

SELECT *
FROM students
WHERE age < id;

SELECT *
FROM students
ORDER BY age ASC;

SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM students s
         LEFT JOIN faculties f ON s.faculty_id = f.id;
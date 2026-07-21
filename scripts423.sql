SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM students s
         JOIN faculties f ON s.faculty_id = f.id;

SELECT
    s.name AS student_name,
    s.age AS student_age
FROM students s
         JOIN avatars a ON s.id = a.student_id;
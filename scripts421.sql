ALTER TABLE faculties
    ADD COLUMN color VARCHAR(50) NOT NULL DEFAULT 'unknown';

ALTER TABLE faculties
    ADD CONSTRAINT unique_faculty_name_color UNIQUE (name, color);

ALTER TABLE students
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT unique_student_name UNIQUE (name);

ALTER TABLE students
    ADD CONSTRAINT check_student_age_min CHECK (age >= 16);

ALTER TABLE students
    ALTER COLUMN age SET DEFAULT 20;
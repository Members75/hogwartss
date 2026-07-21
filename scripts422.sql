CREATE TABLE IF NOT EXISTS car (
                                   id BIGSERIAL PRIMARY KEY,
                                   brand VARCHAR(100) NOT NULL,
                                   model VARCHAR(100) NOT NULL,
                                   price DECIMAL(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS person (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL,
                                      age INT NOT NULL,
                                      has_license BOOLEAN NOT NULL,
                                      car_id BIGINT NOT NULL REFERENCES car(id)
);
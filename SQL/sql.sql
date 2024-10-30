CREATE TABLE meteo (
    id SERIAL PRIMARY KEY,
    citta VARCHAR(100),
    temp_min FLOAT,
    temp_max FLOAT,
    data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

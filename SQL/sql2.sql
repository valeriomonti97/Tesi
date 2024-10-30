CREATE DATABASE weather_db;

\c weather_db;

CREATE TABLE city_weather (
    id SERIAL PRIMARY KEY,
    city_name VARCHAR(100),
    continent VARCHAR(50),
    min_temperature FLOAT,
    max_temperature FLOAT
);

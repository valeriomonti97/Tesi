-- Creare la tabella Continenti
CREATE TABLE IF NOT EXISTS Continenti (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

-- Creare la tabella Citta
CREATE TABLE IF NOT EXISTS Citta (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    continente_id INTEGER REFERENCES Continenti(id)
);

-- Creare la tabella Temperature
CREATE TABLE IF NOT EXISTS Temperature (
    id SERIAL PRIMARY KEY,
    citta_id INTEGER REFERENCES Citta(id),
    data DATE NOT NULL,
    temp_min FLOAT,
    temp_max FLOAT
);

-- Creare la tabella Compagnie Aeree
CREATE TABLE IF NOT EXISTS CompagnieAeree (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Creare la tabella Voli
CREATE TABLE IF NOT EXISTS Voli (
    id SERIAL PRIMARY KEY,
    compagnia_id INTEGER REFERENCES CompagnieAeree(id),
    partenza VARCHAR(100) NOT NULL,
    destinazione VARCHAR(100) NOT NULL,
    orario_partenza TIME,
    orario_arrivo TIME
);


CREATE DATABASE avion;

\c avion;

CREATE TABLE admin (
    id_admin SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255)
);

CREATE EXTENSION  IF NOT EXISTS pgcrypto;
INSERT INTO Admin (username, password) VALUES ('admin', crypt('admin', gen_salt('bf')));
-- 3 types sieges , 2 avions, 6 siege avion, 20 villes  


CREATE TABLE type_siege (
    id_type_siege SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL -- BUSINESS , ECONOMIQUE
);


CREATE TABLE avion (
    id_avion SERIAL PRIMARY KEY,
    compagnie VARCHAR(150), 
    modele VARCHAR(255),
    date_fabrication DATE,
    nombre_max_passager INT
);

CREATE TABLE siege_avion (
    id_siege_avion SERIAL PRIMARY KEY,
    id_type_siege INT REFERENCES type_siege (id_type_siege),
    id_avion INT REFERENCES avion (id_avion),
    nombre_place INT
);


CREATE TABLE ville (
    id_ville SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    pays VARCHAR(255) NOT NULL,
    imageUrl VARCHAR(255)
);


CREATE TABLE vol (
    id_vol SERIAL PRIMARY KEY,
    id_avion INT REFERENCES avion(id_avion),

    id_ville_depart INT REFERENCES ville(id_ville),
    id_ville_destination INT REFERENCES ville(id_ville),

    date_depart TIMESTAMP NOT NULL,
    date_arrive TIMESTAMP NOT NULL,

    CHECK (date_arrive > date_depart)
);

-- montant du vol par siege
CREATE TABLE vol_details (
    id_vol_prix SERIAL PRIMARY KEY,
    id_vol INT REFERENCES vol (id_vol),
    id_type_siege INT REFERENCES type_siege (id_type_siege),
    prix NUMERIC NOT NULL CHECK(prix > 0),
    place_dispo INT NOT NULL CHECK(place_dispo >= 0)
);


CREATE TABLE regle_vol (
    id_regle_vol SERIAL PRIMARY KEY,
    id_vol INT NOT NULL REFERENCES vol(id_vol) ON DELETE CASCADE,  -- Chaque vol a ses propres regles
    heure_max_reservation INT NOT NULL CHECK (heure_max_reservation > 0),  -- Nombre d'heures max avant le vol pour reserver
    heure_max_annulation INT NOT NULL CHECK (heure_max_annulation > 0)  -- Nombre d'heures max avant le vol pour annuler
);

CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    id_vol INT NOT NULL REFERENCES vol(id_vol),  -- Vol associe   
    nombre_personnes INT CHECK (nombre_personnes > 0), -- Nombre de passagers pour cette reservation
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    montant_total DECIMAL(10, 2)
);

CREATE TABLE detail_reservation (
    id_detail SERIAL PRIMARY KEY,
    id_reservation INT NOT NULL REFERENCES reservation(id_reservation) ON DELETE CASCADE,
    nom_complet VARCHAR(255) NOT NULL,
    id_type_siege INT NOT NULL REFERENCES type_siege(id_type_siege),
    prix DECIMAL(10, 2) NOT NULL CHECK(prix > 0)
);


CREATE TABLE promotion (
    id_promotion SERIAL PRIMARY KEY,
    id_vol INT REFERENCES vol (id_vol),
    id_type_siege INT REFERENCES type_siege (id_type_siege),
    pourcentage DECIMAL(10, 2) NOT NULL CHECK(pourcentage > 0 AND pourcentage <= 100),
    nombre_max_passager INT NOT NULL CHECK(nombre_max_passager > 0)
);

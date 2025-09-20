-- Base de données
DROP DATABASE IF EXISTS gestion_rh;
CREATE DATABASE gestion_rh;
\c gestion_rh;

-- Table diplome
CREATE TABLE diplome (
    id SERIAL PRIMARY KEY,
    nom_diplome VARCHAR(30) NOT NULL
);

-- Table filiere
CREATE TABLE filiere (
    id SERIAL PRIMARY KEY,
    nom_filiere VARCHAR(30) NOT NULL
);

-- Table qualite
CREATE TABLE qualite (
    id SERIAL PRIMARY KEY,
    nom_qualite VARCHAR(30) NOT NULL
);

-- Table mission
CREATE TABLE mission (
    id SERIAL PRIMARY KEY,
    nom_mission VARCHAR(30) NOT NULL
);

-- Table critere_rech
CREATE TABLE critere_rech (
    id SERIAL PRIMARY KEY,
    annees_experience INT NOT NULL,
    diplome INT NOT NULL REFERENCES diplome(id),
    age INT NOT NULL,
    genre SMALLINT NOT NULL,
    filiere INT NOT NULL REFERENCES filiere(id)
);

-- Table annonce (plus de DEFAULT CURRENT_DATE)
CREATE TABLE annonce (
    id SERIAL PRIMARY KEY,
    profil VARCHAR(40) NOT NULL,
    description VARCHAR(120) NOT NULL,
    critere_rech_id INT NOT NULL REFERENCES critere_rech(id),
    date_publication DATE NOT NULL
);

-- Table liaison critere_rech ↔ qualite
CREATE TABLE critere_rech_qualite (
    id SERIAL PRIMARY KEY,
    id_critere INT NOT NULL REFERENCES critere_rech(id),
    id_qualite INT NOT NULL REFERENCES qualite(id)
);

-- Table liaison critere_rech ↔ mission
CREATE TABLE critere_rech_mission (
    id SERIAL PRIMARY KEY,
    id_critere INT NOT NULL REFERENCES critere_rech(id),
    id_mission INT NOT NULL REFERENCES mission(id)
);

-- Diplômes
INSERT INTO diplome (nom_diplome) VALUES
 ('CEPE'), ('BEPC'), ('Baccalauréat'),
 ('Licence'), ('Master'), ('Doctorat');

-- Filières
INSERT INTO filiere (nom_filiere) VALUES
 ('Informatique'), ('Gestion'), ('Santé'),
 ('Droit'), ('Tourisme');

-- Qualités
INSERT INTO qualite (nom_qualite) VALUES
 ('Ponctualité'), ('Créativité'), ('Travail en équipe'),
 ('Leadership'), ('Organisation');

-- Missions
INSERT INTO mission (nom_mission) VALUES
 ('Gestion des stocks'), ('Accueil clients'),
 ('Préparation rapports'), ('Encadrement du personnel'),
 ('Organisation événements');

-- Exemple de critère
INSERT INTO critere_rech (annees_experience, diplome, age, genre, filiere)
VALUES (2, 4, 25, 1, 1);  -- Licence, 25 ans, Homme, Informatique

-- Exemple d’annonce avec date posée
INSERT INTO annonce (profil, description, critere_rech_id, date_publication)
VALUES 
 ('Développeur Java', 'Développement et maintenance des applications', 1, DATE '2025-01-15'),
 ('Comptable', 'Gestion des écritures comptables et bilans financiers', 1, DATE '2025-02-10'),
 ('Guide touristique', 'Accompagner les visiteurs et expliquer les sites', 1, DATE '2025-03-05');

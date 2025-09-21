-- Base de données
DROP DATABASE IF EXISTS gestion_rh_v;
CREATE DATABASE gestion_rh_v;
\c gestion_rh_v;

-- Table diplome
CREATE TABLE diplome (
    id SERIAL PRIMARY KEY,
    nom_diplome VARCHAR(30) NOT NULL
);
CREATE TABLE genre (
    id SERIAL PRIMARY KEY,
    genre VARCHAR(20)
);
-- Table filiere
CREATE TABLE filiere (
    id SERIAL PRIMARY KEY,
    nom_filiere VARCHAR(30) NOT NULL
);
CREATE TABLE situation_matrimonial (
    id SERIAL PRIMARY KEY,
    situation VARCHAR(30)
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
INSERT INTO genre (genre) VALUES
('Homme'),
('Femme');

-- Diplômes
INSERT INTO diplome (nom_diplome) VALUES
('Bepc'),
('Baccalauréat'),
('Licence'),
('Master'),
('Doctorat');

-- Filières
INSERT INTO filiere (nom_filiere) VALUES
('Informatique'),
('Gestion'),
('Commerce'),
('Droit'),
('Médecine');

-- Situations matrimoniales
INSERT INTO situation_matrimonial (situation) VALUES
('Célibataire'),
('Marié(e)'),
('Divorcé(e)'),
('Veuf/Veuve');
-- Table critere_rech
CREATE TABLE critere_rech (
    id SERIAL PRIMARY KEY,
    annees_experience INT NOT NULL,
    diplome INT NOT NULL REFERENCES diplome(id),
    age INT NOT NULL,
    genre SMALLINT NOT NULL,
    filiere INT NOT NULL REFERENCES filiere(id)
);

CREATE TABLE poste (
    id SERIAL PRIMARY KEY,
    profil VARCHAR(50),
    description VARCHAR(120)
);

-- Table annonce (plus de DEFAULT CURRENT_DATE)
CREATE TABLE annonce (
    id SERIAL PRIMARY KEY,
    critere_rech_id INT NOT NULL REFERENCES critere_rech(id),
    date_publication DATE NOT NULL,
    id_poste INT NOT NULL REFERENCES poste(id)
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
-- INSERT INTO diplome (nom_diplome) VALUES
--  ('CEPE'), ('BEPC'), ('Baccalauréat'),
--  ('Licence'), ('Master'), ('Doctorat');

-- Filières
INSERT INTO filiere (nom_filiere) VALUES
 ('Informatique'), ('Gestion'), ('Santé'),
 ('Droit'), ('Tourisme');

INSERT INTO poste(profil, description) VALUES 
 ('Développeur Java', 'Développement et maintenance des applications'),('Comptable', 'Gestion des écritures comptables et bilans financiers'),('Guide touristique', 'Accompagner les visiteurs et expliquer les sites');
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
INSERT INTO annonce (critere_rech_id, date_publication, id_poste)
VALUES 
 (1, DATE '2025-01-15', 1),
 (1, DATE '2025-02-10', 2),
 (1, DATE '2025-03-05',3);


CREATE TABLE candidat (
    id SERIAL PRIMARY KEY,
    id_annonce INT REFERENCES annonce(id),
    nom VARCHAR(40),
    prenom VARCHAR(40),
    age INT,
    genre INT REFERENCES genre(id),
    adresse VARCHAR(60),
    email VARCHAR(40),
    annees_experience INT,
    lettre_motivation VARCHAR(300),
    cv VARCHAR(60),
    date_candidature DATE DEFAULT CURRENT_DATE,
    id_diplome INT REFERENCES diplome(id)
);

CREATE TABLE info_perssonelle (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    date_naissance DATE,
    lieu_naissance VARCHAR(50),
    adresse VARCHAR(50),
    situation_matrimonial INT REFERENCES situation_matrimonial(id)
);

CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    info_perssonel INT REFERENCES info_perssonelle(id)
);

CREATE TABLE poste_employe (
    id SERIAL PRIMARY KEY,
    id_employee INT REFERENCES employee(id),
    id_poste INT REFERENCES poste(id),
    last_date DATE
);

CREATE TABLE affiliation_organisme (
    id SERIAL PRIMARY KEY,
    idEmploye INT REFERENCES employee(id),
    idOrganisme INT REFERENCES organisme(id)
);

-- ===============================
-- TABLES QCM ET ÉVALUATION
-- ===============================

CREATE TABLE qcm_questions (
    id SERIAL PRIMARY KEY,
    question VARCHAR(100)
);

CREATE TABLE qcm_reponses (
    id SERIAL PRIMARY KEY,
    id_questions INT REFERENCES qcm_questions(id),
    reponse VARCHAR(100)
);

CREATE TABLE bareme_notation (
    id SERIAL PRIMARY KEY,
    id_question INT REFERENCES qcm_questions(id),
    valeur_question DOUBLE PRECISION
);

CREATE TABLE bareme_entretien (
    id SERIAL PRIMARY KEY,
    id_annonce INT REFERENCES annonce(id),
    bareme DOUBLE PRECISION
);

CREATE TABLE historique_score (
    id SERIAL PRIMARY KEY,
    id_annonce INT REFERENCES annonce(id),
    id_candidat INT REFERENCES candidat(id),
    score DOUBLE PRECISION
);

-- ===============================
-- TABLES CONTRATS
-- ===============================

CREATE TABLE contrat_essai (
    id SERIAL PRIMARY KEY,
    id_candidat INT REFERENCES candidat(id),
    id_poste INT REFERENCES poste(id),
    date_debut DATE,
    duree INT, -- en jours
    date_fin DATE,
    salaire DOUBLE PRECISION,
    conditions TEXT,
    etat VARCHAR(20) DEFAULT 'En attente'
);

CREATE TABLE historique_contrat_essai (
    id SERIAL PRIMARY KEY,
    id_candidat INT REFERENCES candidat(id),
    duree INT,
    dateDebutContrat DATE
);

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- mot de passe haché
    role VARCHAR(20) CHECK (role IN ('admin', 'rh', 'client')) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Un admin
INSERT INTO utilisateur (username, password, role) 
VALUES ('admin1', 'hash_mdp_admin', 'admin');

-- Un RH
INSERT INTO utilisateur (username, password, role) 
VALUES ('rh1', 'hash_mdp_rh', 'rh');

-- Un client
INSERT INTO utilisateur (username, password, role) 
VALUES ('client1', '    ', 'client');



-- ===============================
-- INSERTIONS DE BASE
-- ===============================

-- Genres


-- Exemples de candidats
INSERT INTO candidat (id_annonce, nom, prenom, age, genre, adresse, email, annees_experience, lettre_motivation, cv, id_diplome)
VALUES
(1, 'Rakoto', 'Jean', 25, 1, 'Antananarivo', 'jean.rakoto@email.com', 2, 
 'Motivé pour évoluer dans votre entreprise', 'cv_jean.pdf', 2),

(1, 'Rabe', 'Marie', 27, 2, 'Toamasina', 'marie.rabe@email.com', 4,
 'Expérimentée et dynamique', 'cv_marie.pdf', 3),

(2, 'Randria', 'Paul', 30, 1, 'Fianarantsoa', 'paul.randria@email.com', 6,
 'Prêt à relever des défis', 'cv_paul.pdf', 4);

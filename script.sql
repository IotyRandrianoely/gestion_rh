-- Création de la base
CREATE DATABASE gestion_rh;
\c gestion_rh;

-- ===============================
-- TABLES DE RÉFÉRENCE
-- ===============================

CREATE TABLE genre (
    id SERIAL PRIMARY KEY,
    genre VARCHAR(20)
);

CREATE TABLE diplome (
    id SERIAL PRIMARY KEY,
    nom_diplome VARCHAR(30)
);

CREATE TABLE filiere (
    id SERIAL PRIMARY KEY,
    nom_filiere VARCHAR(30)
);

CREATE TABLE situation_matrimonial (
    id SERIAL PRIMARY KEY,
    situation VARCHAR(30)
);

CREATE TABLE poste (
    id SERIAL PRIMARY KEY,
    nom_poste VARCHAR(30)
);

CREATE TABLE departement (
    id SERIAL PRIMARY KEY,
    nomDept VARCHAR(30)
);

CREATE TABLE organisme (
    id SERIAL PRIMARY KEY,
    nomOrganisme VARCHAR(30),
    detail VARCHAR(30)
);

-- ===============================
-- TABLES ANNONCE ET CRITÈRES
-- ===============================

CREATE TABLE critere_rech (
    id SERIAL PRIMARY KEY,
    annees_experience INT,
    diplome INT REFERENCES diplome(id),
    age INT,
    genre INT REFERENCES genre(id),
    filiere INT REFERENCES filiere(id)
);

CREATE TABLE annonce (
    id SERIAL PRIMARY KEY,
    profil VARCHAR(40),
    description VARCHAR(120),
    critere_rech_id INT REFERENCES critere_rech(id),
    date_publication DATE DEFAULT CURRENT_DATE
);

CREATE TABLE qualite (
    id SERIAL PRIMARY KEY,
    nom_qualite VARCHAR(30)
);

CREATE TABLE mission (
    id SERIAL PRIMARY KEY,
    nom_mission VARCHAR(30)
);

CREATE TABLE critere_rech_qualite (
    id_annonce INT REFERENCES annonce(id),
    id_qualite INT REFERENCES qualite(id)
);

CREATE TABLE critere_rech_mission (
    id_annonce INT REFERENCES annonce(id),
    id_mission INT REFERENCES mission(id)
);

CREATE TABLE historique_annonce (
    id SERIAL PRIMARY KEY,
    id_annonce INT REFERENCES annonce(id),
    etat INT,
    date_historique DATE
);

-- ===============================
-- TABLES CANDIDATS ET EMPLOYÉS
-- ===============================

CREATE TABLE candidat (²
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

-- Exemples de candidats
INSERT INTO candidat (id_annonce, nom, prenom, age, genre, adresse, email, annees_experience, lettre_motivation, cv, id_diplome)
VALUES
(1, 'Rakoto', 'Jean', 25, 1, 'Antananarivo', 'jean.rakoto@email.com', 2, 
 'Motivé pour évoluer dans votre entreprise', 'cv_jean.pdf', 2),

(1, 'Rabe', 'Marie', 27, 2, 'Toamasina', 'marie.rabe@email.com', 4,
 'Expérimentée et dynamique', 'cv_marie.pdf', 3),

(2, 'Randria', 'Paul', 30, 1, 'Fianarantsoa', 'paul.randria@email.com', 6,
 'Prêt à relever des défis', 'cv_paul.pdf', 4);

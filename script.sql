create database gestion_rh;
\c gestion_rh;
create table annonce(
    id INT,
    profil varchar(40),
    description VARCHAR(120),
    critere_rech INT,
    date_publication DATE
);
create table critere_rech(
    id SERIAL PRIMARY KEY,
    annees_experience INT,
    diplome INT,
    age INT,
    genre INT,
    filiere INT
);
create table qualite(
    id INT,
    nom_qualite VARCHAR(30)
);
create table mission(
    id INT,
    nom_mission VARCHAR(30)
);
create table diplome(
    id INT,
    nom_diplome VARCHAR(30)
);
create table filiere(
    id INT,
    nom_filiere VARCHAR(30)
);
create table critere_rech_qualite(
    id_annonce INT,
    id_qualite INT
);
create table critere_rech_mission(
    id_annonce INT,
    id_mission INT
);

create table historique_annonce(
    id INT,
    id_annonce INT,
    etat INT ,
    date_historique DATE 
);
create table candidat(
    id INT,
    id_annonce INT,
    nom VARCHAR(40),
    prenom VARCHAR(40),
    age INT,
    genre INT,
    adresse VARCHAR(60),
    email VARCHAR(40),
    annees_experience INT,
    lettre_motivation varchar(300),
    cv VARCHAR(60),
    date_candidature DATE
);
create table genre(
    id INT,
    genre VARCHAR(20)
);
create table employee(
    id INT,
    info_pressonel INT
);
create table info_perssonelle(
    id INT,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    date_naissance DATE,
    lieu_naissance VARCHAR(50),
    adresse VARCHAR(50),
    situation_matrimonial INT,
    employee INT,
    situation_matrimonial VARCHAR,
);
create table info_professionelle(

);
create table situation_matrimonial(
    id int,
    situation VARCHAR(30);
);
create table poste(
    id INT,
    nom_poste varchar(30),
    
);
create table poste_employe(
    id INT,
    id_employee INT,
    id_poste INT,
    last_date DATE
);
create table departement(
    id INT,
    nomDept varchar(30)
);
create table qcm_questions(
    id INT,
    question VARCHAR(30)
);
create table qcm_reponses(
    id INT,
    id_questions INT,
    reponse VARCHAR(40)
);
create table bareme_notation(
    id INT,
    id_question INT,
    valeur_question double
);
create table bareme_entretien(
    id INT,
    id_annonce INT,
    bareme double
);
create table historique_score(
    id INT,
    id_annonce INT,
    id_candidat INT,
    score double
);
io

create table affiliation_organisme(
    id INT,
    idEmploye INT,
    idOrganisme INT
);
create table organisme(
    id INT,
    nomOrganisme VARCHAR(30),
    detail VARCHAR(30)
);

// lysa
create table contrat_essai (
    id INT primary key auto_increment,
    id_candidat INT,
    id_poste INT,
    date_debut DATE,
    duree INT, -- en jours
    date_fin DATE,
    salaire DOUBLE,
    conditions TEXT,
    etat VARCHAR(20) default 'En attente',
    foreign key (id_candidat) references candidat(id),
    foreign key (id_poste) references poste(id)
);

create table historique_contrat_essai(
    id INT,
    id_candidat INT,
    duree INT,
    dateDebutContrat DATE
);
ALTER TABLE critere_rech
ADD COLUMN id SERIAL PRIMARY KEY;

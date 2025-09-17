-- Annonces
insert into annonce(id, profil, description, critere_rech, date_publication) values
(1, 'Développeur Java', 'Développement d''applications web', 1, '2025-09-01'),
(2, 'Analyste RH', 'Gestion des ressources humaines', 2, '2025-09-05');

-- Critères de recherche
insert into critere_rech(annees_experience, diplome, age, genre, filiere) values
(2, 1, 25, 1, 1),
(5, 2, 30, 2, 2);

-- Qualités
insert into qualite(id, nom_qualite) values
(1, 'Rigueur'),
(2, 'Travail en équipe');

-- Missions
insert into mission(id, nom_mission) values
(1, 'Développement'),
(2, 'Analyse');

-- Diplômes
insert into diplome(id, nom_diplome) values
(1, 'Licence Informatique'),
(2, 'Master RH');

-- Filières
insert into filiere(id, nom_filiere) values
(1, 'Informatique'),
(2, 'Ressources Humaines');

-- critere_rech_qualite
insert into critere_rech_qualite(id_annonce, id_qualite) values
(1, 1),
(1, 2),
(2, 2);

-- critere_rech_mission
insert into critere_rech_mission(id_annonce, id_mission) values
(1, 1),
(2, 2);

-- Genre
insert into genre(id, genre) values
(1, 'Homme'),
(2, 'Femme');

-- Candidats
insert into candidat(id, id_annonce, nom, prenom, age, genre, adresse, email, annees_experience, lettre_motivation, cv, date_candidature) values
(1, 1, 'Dupont', 'Jean', 26, 1, '123 rue A', 'jean.dupont@mail.com', 3, 'Motivé et passionné.', 'cv_jean.pdf', '2025-09-10'),
(2, 2, 'Martin', 'Sophie', 31, 2, '456 rue B', 'sophie.martin@mail.com', 6, 'Expérience confirmée.', 'cv_sophie.pdf', '2025-09-12');
insert into poste(id, nom_poste) values
(1, 'Développeur Java'),
(2, 'Analyste RH'),
(3, 'Chef de projet');
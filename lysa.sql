create table contrat_essai (
    id SERIAL PRIMARY KEY,
    id_candidat INT,
    id_poste INT,
    date_debut DATE,
    duree INT, -- en jours
    date_fin DATE,
    salaire NUMERIC(10,2),
    foreign key (id_candidat) references candidat(id),
    foreign key (id_poste) references poste(id)
);

create table planing_entretien (
    id SERIAL PRIMARY KEY,
    id_candidat INT,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP
);

INSERT INTO candidat (
    id_annonce, nom, prenom, age, genre, adresse, email, 
    annees_experience, lettre_motivation, cv, date_candidature
) VALUES
-- Candidats pour une annonce developpeur
(1, 'Rakoto', 'Jean', 28, 1, 'Antananarivo', 'jean.rakoto@example.com', 
  3, 'Passionne par le developpement backend et motive a rejoindre votre equipe', 
  'cv_jean.pdf', '2025-09-15'),

(1, 'Randria', 'Aina', 25, 2, 'Antsirabe', 'aina.randria@example.com', 
  2, 'Je souhaite contribuer a des projets innovants et apprendre de vos experts', 
  'cv_aina.pdf', '2025-09-16'),

-- Candidats pour une annonce comptable
(2, 'Rasoa', 'Mialy', 32, 2, 'Toamasina', 'mialy.rasoa@example.com', 
  7, 'Rigoureuse et organisee je cherche a integrer votre service comptabilite', 
  'cv_mialy.pdf', '2025-09-12'),

(2, 'Rakotobe', 'Hery', 40, 1, 'Fianarantsoa', 'hery.rakotobe@example.com', 
  15, 'Fort dune longue experience en comptabilite je souhaite relever de nouveaux defis', 
  'cv_hery.pdf', '2025-09-10'),

-- Candidats pour une annonce marketing
(3, 'Andrianina', 'Tiana', 27, 1, 'Mahajanga', 'tiana.andrianina@example.com', 
  4, 'Creatif et motive jaimerais contribuer a la strategie marketing digitale', 
  'cv_tiana.pdf', '2025-09-13'),

(3, 'Rakotomanga', 'Lova', 30, 2, 'Antananarivo', 'lova.rakotomanga@example.com', 
  5, 'Avec mon experience en communication je suis prete a booster vos campagnes', 
  'cv_lova.pdf', '2025-09-14');

-- 2 entretiens le même jour (2025-09-20)
INSERT INTO planing_entretien (id_candidat, date_debut, date_fin)
VALUES 
    (1, '2025-09-20 09:00:00', '2025-09-20 10:00:00'),
    (2, '2025-09-20 14:00:00', '2025-09-20 15:30:00');

-- Entretiens en octobre 2025
INSERT INTO planing_entretien (id_candidat, date_debut, date_fin)
VALUES 
    (3, '2025-10-05 11:00:00', '2025-10-05 12:00:00'),
    (4, '2025-10-18 16:00:00', '2025-10-18 17:00:00');

-- Entretiens dans une autre année (2026)
INSERT INTO planing_entretien (id_candidat, date_debut, date_fin)
VALUES 
    (5, '2026-01-12 09:30:00', '2026-01-12 10:30:00'),
    (6, '2026-03-22 13:00:00', '2026-03-22 14:30:00');
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
 ('RH'),('Informatique'),('Commerce'), ('Finance'), 
 ('Marketing');

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

-- Exemple d annonce avec date posée
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

CREATE TABLE qcm_questions (
    question_id SERIAL PRIMARY KEY,
    entity_id INT,
    question_text TEXT NOT NULL,
    FOREIGN KEY (entity_id) REFERENCES filiere(id)
);
CREATE TABLE qcm_options (
    option_id SERIAL PRIMARY KEY,
    question_id INT,
    option_text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (question_id) REFERENCES qcm_questions(question_id)
);
INSERT INTO qcm_questions (entity_id, question_text) VALUES
(1, 'Pouvez-vous vous présenter en quelques mots ?'),
(1, 'Quelles sont vos principales forces et vos points à améliorer ?'),
(1, 'Comment gérez-vous le stress ou la pression au travail ?'),
(1, 'Pouvez-vous donner un exemple de conflit que vous avez résolu ?'),
(1, 'Qu est-ce qui vous motive le plus dans votre travail ?'),
(1, 'Où vous voyez-vous dans 3 à 5 ans ?'),
(1, 'Préférez-vous travailler seul ou en équipe ? Pourquoi ?'),
(1, 'Quelles sont vos attentes vis-à-vis d un manager ?'),
(1, 'Pouvez-vous citer une expérience professionnelle dont vous êtes fier ?'),
(1, 'Comment organisez-vous vos priorités quand plusieurs tâches sont urgentes ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
-- Q1
(1, 'Réponse claire et structurée, adaptée au poste', TRUE),
(1, 'Discours confus ou hors sujet', FALSE),
(1, 'Trop de détails personnels', FALSE),

-- Q2
(2, 'Capacité d auto-évaluation honnête', TRUE),
(2, 'Aucune faiblesse mentionnée', FALSE),
(2, 'Forces non pertinentes pour le poste', FALSE),

-- Q3
(3, 'Utilisation de méthodes de gestion du stress (organisation, pause, sport, etc.)', TRUE),
(3, 'Ignorer complètement le stress', FALSE),
(3, 'Reporter la faute sur les autres', FALSE),

-- Q4
(4, 'Communication et recherche de compromis', TRUE),
(4, 'Évitement total du problème', FALSE),
(4, 'Conflit aggravé volontairement', FALSE),

-- Q5
(5, 'Motivation intrinsèque (apprentissage, défis, passion)', TRUE),
(5, 'Motivation uniquement financière', FALSE),
(5, 'Aucune motivation exprimée', FALSE),

-- Q6
(6, 'Projection réaliste et cohérente avec le poste', TRUE),
(6, 'Aucune idée de son avenir', FALSE),
(6, 'Ambitions irréalistes immédiates (ex: être PDG demain)', FALSE),

-- Q7
(7, 'Valorisation du travail en équipe (collaboration, complémentarité)', TRUE),
(7, 'Préférence excessive pour le travail isolé', FALSE),
(7, 'Indifférence totale', FALSE),

-- Q8
(8, 'Attentes claires (communication, soutien, reconnaissance)', TRUE),
(8, 'Attentes irréalistes (aucune contrainte, liberté totale)', FALSE),
(8, 'Aucune attente exprimée', FALSE),

-- Q9
(9, 'Exemple concret, lié au poste, montrant une compétence clé', TRUE),
(9, 'Exemple sans rapport avec le travail', FALSE),
(9, 'Aucun exemple donné', FALSE),

-- Q10
(10, 'Organisation par priorités et délais', TRUE),
(10, 'Faire tout en même temps sans méthode', FALSE),
(10, 'Laisser les tâches urgentes de côté', FALSE);

INSERT INTO qcm_questions (entity_id, question_text) VALUES
(2, 'Quels sont les avantages principaux de l''utilisation d''un modèle MVC dans le développement web ?'),
(2, 'Quelles sont les techniques utilisées pour prévenir les attaques par injection SQL ?'),
(2, 'Quels protocoles sont utilisés pour sécuriser les communications sur Internet ?'),
(2, 'Quels sont les principes fondamentaux de l''agilité selon le Manifeste Agile ?'),
(2, 'Quels sont les types de bases de données relationnelles ?'),
(2, 'Quels sont les avantages de l''utilisation des conteneurs comme Docker ?'),
(2, 'Quels sont les objectifs principaux de la méthode DevOps ?'),
(2, 'Quels langages sont couramment utilisés pour le développement web côté client ?'),
(2, 'Quels sont les risques associés à une mauvaise gestion des dépendances dans un projet logiciel ?'),
(2, 'Quelles sont les étapes clés du cycle de vie du développement logiciel (SDLC) ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(11, 'Réduction de la taille du code', FALSE),
(11, 'Séparation des préoccupations (logique, interface, données)', TRUE),
(11, 'Augmentation de la vitesse d''exécution', FALSE),
(11, 'Meilleure modularité et maintenance', TRUE),
(12, 'Utilisation de pare-feu', FALSE),
(12, 'Requêtes paramétrées', TRUE),
(12, 'Validation des entrées utilisateur', TRUE),
(12, 'Augmentation de la bande passante', FALSE),
(13, 'FTP', FALSE),
(13, 'HTTPS', TRUE),
(13, 'SSH', TRUE),
(13, 'SMTP', FALSE),
(14, 'Collaboration et communication continues', TRUE),
(14, 'Automatisation des tests', TRUE),
(14, 'Optimisation des profits', FALSE),
(14, 'Réduction des effectifs', FALSE),
(15, 'MySQL', TRUE),
(15, 'MongoDB', FALSE),
(15, 'PostgreSQL', TRUE),
(15, 'Redis', FALSE),
(16, 'Portabilité des applications', TRUE),
(16, 'Augmentation des coûts matériels', FALSE),
(16, 'Isolation des environnements', TRUE),
(16, 'Complexité accrue du code', FALSE),
(17, 'Amélioration de la collaboration entre développement et opérations', TRUE),
(17, 'Réduction du temps de mise sur le marché', TRUE),
(17, 'Augmentation des coûts de maintenance', FALSE),
(17, 'Centralisation des données', FALSE),
(18, 'JavaScript', TRUE),
(18, 'Python', FALSE),
(18, 'HTML/CSS', TRUE),
(18, 'SQL', FALSE),
(19, 'Conflits de versions', TRUE),
(19, 'Augmentation de la sécurité', FALSE),
(19, 'Retards dans les déploiements', TRUE),
(19, 'Réduction des coûts', FALSE),
(20, 'Analyse des besoins', TRUE),
(20, 'Conception', TRUE),
(20, 'Marketing du produit', FALSE),
(20, 'Maintenance', TRUE);



INSERT INTO qcm_questions (entity_id, question_text) VALUES
(3, 'Quels sont les éléments clés d''une stratégie de vente consultative ?'),
(3, 'Quels outils sont essentiels pour la gestion des relations client ?'),
(3, 'Quels indicateurs mesurent la performance commerciale ?'),
(3, 'Quels sont les avantages d''une segmentation de marché ?'),
(3, 'Quelles sont les étapes du processus de vente ?'),
(3, 'Quels sont les types de canaux de distribution ?'),
(3, 'Quelles sont les techniques de négociation efficaces ?'),
(3, 'Quels sont les objectifs d''une campagne de prospection ?'),
(3, 'Quels facteurs influencent la fidélité des clients ?'),
(3, 'Quels sont les avantages de l''analyse des données commerciales ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(21, 'Identifier les besoins du client', TRUE),
(21, 'Proposer des solutions adaptées', TRUE),
(21, 'Vendre le produit le moins cher', FALSE),
(21, 'Automatiser les ventes', FALSE),
(22, 'CRM (Customer Relationship Management)', TRUE),
(22, 'ERP (Enterprise Resource Planning)', FALSE),
(22, 'Logiciels de comptabilité', FALSE),
(22, 'Outils d''automatisation marketing', TRUE),
(23, 'NPS (Net Promoter Score)', TRUE),
(23, 'ROI (Return on Investment)', TRUE),
(23, 'Taux de conversion', TRUE),
(23, 'Nombre d''employés', FALSE),
(24, 'Meilleure compréhension des clients', TRUE),
(24, 'Réduction des coûts publicitaires', FALSE),
(24, 'Ciblage plus précis', TRUE),
(24, 'Augmentation des effectifs', FALSE),
(25, 'Prospection', TRUE),
(25, 'Négociation', TRUE),
(25, 'Clôture', TRUE),
(25, 'Production', FALSE),
(26, 'Vente directe', TRUE),
(26, 'Vente en ligne', TRUE),
(26, 'Vente par correspondance', TRUE),
(26, 'Vente interne', FALSE),
(27, 'Écoute active', TRUE),
(27, 'Proposition de valeur claire', TRUE),
(27, 'Augmentation des prix', FALSE),
(27, 'Flexibilité', TRUE),
(28, 'Augmenter le chiffre d''affaires', TRUE),
(28, 'Identifier de nouveaux clients', TRUE),
(28, 'Réduire les coûts', FALSE),
(28, 'Améliorer la logistique', FALSE),
(29, 'Qualité du service', TRUE),
(29, 'Expérience client', TRUE),
(29, 'Prix compétitifs', TRUE),
(29, 'Taille de l''entreprise', FALSE),
(30, 'Prise de décision éclairée', TRUE),
(30, 'Optimisation des campagnes', TRUE),
(30, 'Réduction des effectifs', FALSE),
(30, 'Augmentation des bénéfices', TRUE);


INSERT INTO qcm_questions (entity_id, question_text) VALUES
(4, 'Quels sont les éléments principaux d''un bilan comptable ?'),
(4, 'Quels sont les ratios financiers clés pour évaluer la santé d''une entreprise ?'),
(4, 'Quels sont les objectifs de l''analyse financière ?'),
(4, 'Quels sont les types de budgets en entreprise ?'),
(4, 'Quelles sont les sources de financement pour une entreprise ?'),
(4, 'Quels sont les principes comptables généralement acceptés (PCGA) ?'),
(4, 'Quels sont les avantages de la gestion de la trésorerie ?'),
(4, 'Quelles sont les conséquences d''une mauvaise gestion financière ?'),
(4, 'Quels sont les outils d''analyse financière ?'),
(4, 'Quels sont les types de coûts en comptabilité ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(31, 'Actif', TRUE),
(31, 'Passif', TRUE),
(31, 'Chiffre d''affaires', FALSE),
(31, 'Capitaux propres', TRUE),
(32, 'Ratio d''endettement', TRUE),
(32, 'Ratio de liquidité', TRUE),
(32, 'Taux de conversion', FALSE),
(32, 'Marge brute', TRUE),..
(33, 'Évaluer la rentabilité', TRUE),
(33, 'Analyser la solvabilité', TRUE),
(33, 'Augmenter les ventes', FALSE),
(33, 'Prévoir les performances futures', TRUE),
(34, 'Budget opérationnel', TRUE),
(34, 'Budget d''investissement', TRUE),
(34, 'Budget marketing', TRUE),
(34, 'Budget RH', FALSE),
(35, 'Fonds propres', TRUE),
(35, 'Emprunts bancaires', TRUE),
(35, 'Subventions', TRUE),
(35, 'Ventes de produits', FALSE),
(36, 'Principe de prudence', TRUE),
(36, 'Principe de continuité', TRUE),
(36, 'Principe de non-compensation', TRUE),
(36, 'Principe de surévaluation', FALSE),
(37, 'Éviter les problèmes de liquidité', TRUE),
(37, 'Optimiser les investissements', TRUE),
(37, 'Réduire les effectifs', FALSE),
(37, 'Améliorer la planification', TRUE),
(38, 'Insolvabilité', TRUE),
(38, 'Pertes financières', TRUE),
(38, 'Augmentation des ventes', FALSE),
(38, 'Mauvaise réputation', TRUE),
(39, 'Tableau de flux de trésorerie', TRUE),
(39, 'Analyse SWOT', FALSE),
(39, 'Ratios financiers', TRUE),
(39, 'Compte de résultat', TRUE),
(40, 'Coûts fixes', TRUE),
(40, 'Coûts variables', TRUE),
(40, 'Coûts directs', TRUE),
(40, 'Coûts marketing', FALSE);


INSERT INTO qcm_questions (entity_id, question_text) VALUES
(5, 'Quels sont les objectifs principaux du marketing de contenu ?'),
(5, 'Quelles sont les composantes du mix marketing (4P) ?'),
(5, 'Quels sont les avantages du SEO (optimisation pour les moteurs de recherche) ?'),
(5, 'Quels sont les rôles des personas en marketing ?'),
(5, 'Quels sont les types de campagnes publicitaires digitales ?'),
(5, 'Quelles sont les métriques clés pour évaluer une campagne marketing ?'),
(5, 'Quels sont les avantages des réseaux sociaux pour le marketing ?'),
(5, 'Quelles sont les étapes du parcours client ?'),
(5, 'Quels sont les outils d''automatisation marketing ?'),
(5, 'Quels sont les principes de l''inbound marketing ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(41, 'Attirer une audience', TRUE),
(41, 'Engager les clients', TRUE),
(41, 'Réduire les coûts', FALSE),
(41, 'Fidéliser les clients', TRUE),
(42, 'Produit', TRUE),
(42, 'Prix', TRUE),
(42, 'Promotion', TRUE),
(42, 'Performance', FALSE),
(43, 'Augmentation du trafic organique', TRUE),
(43, 'Amélioration du classement dans les moteurs de recherche', TRUE),
(43, 'Réduction des coûts publicitaires', TRUE),
(43, 'Automatisation des ventes', FALSE),
(44, 'Définir les profils des clients cibles', TRUE),
(44, 'Personnaliser les campagnes', TRUE),
(44, 'Réduire les budgets', FALSE),
(44, 'Analyser les performances', TRUE),
(45, 'Publicité display', TRUE),
(45, 'Publicité sur les réseaux sociaux', TRUE),
(45, 'Publicité par e-mail', TRUE),
(45, 'Publicité imprimée', FALSE),
(46, 'Taux de clics (CTR)', TRUE),
(46, 'Taux de conversion', TRUE),
(46, 'Nombre d''employés', FALSE),
(46, 'Coût par acquisition (CPA)', TRUE),
(47, 'Visibilité accrue', TRUE),
(47, 'Engagement avec les clients', TRUE),
(47, 'Réduction des coûts marketing', FALSE),
(47, 'Interaction en temps réel', TRUE),
(48, 'Prise de conscience', TRUE),
(48, 'Considération', TRUE),
(48, 'Décision', TRUE),
(48, 'Production', FALSE),
(49, 'HubSpot', TRUE),
(49, 'Mailchimp', TRUE),
(49, 'SAP', FALSE),
(49, 'Marketo', TRUE),
(50, 'Attirer avec du contenu pertinent', TRUE),
(50, 'Convertir les prospects', TRUE),
(50, 'Fidéliser les clients', TRUE),
(50, 'Réduire les effectifs', FALSE);


CREATE TABLE bareme_notation (
    id SERIAL PRIMARY KEY,
    id_question INT REFERENCES qcm_questions(question_id),
    valeur_question DOUBLE PRECISION
);

CREATE TABLE bareme_entretien (
    id SERIAL PRIMARY KEY,
    id_annonce INT REFERENCES annonce(id),
    bareme DOUBLE PRECISION
);
CREATE TABLE resultat_entretien (
    id BIGSERIAL PRIMARY KEY,           -- auto-incrément, équivalent à @GeneratedValue(strategy=IDENTITY)
    id_candidat INTEGER NOT NULL,       -- identifiant du candidat (colonne non nulle)
    niveau INTEGER NOT NULL             -- niveau du candidat à l’entretien
);

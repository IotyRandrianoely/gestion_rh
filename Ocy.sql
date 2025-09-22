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
INSERT INTO filiere (id,nom_filiere) VALUES
(1,'Informatique'),
(2,'Gestion'),
(3,'Commerce'),
(4,'Droit'),
(5,'Médecine');

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
 (4, DATE '2025-01-15', 1),
 (4, DATE '2025-02-10', 2),
 (4, DATE '2025-03-05',3);


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
INSERT INTO candidat (id,id_annonce, nom, prenom, age, genre, adresse, email, annees_experience, lettre_motivation, cv, id_diplome,est_propose)
VALUES
(1,10, 'Rakoto', 'Jean', 25, 1, 'Antananarivo', 'jean.rakoto@email.com', 2, 
 'Motivé pour évoluer dans votre entreprise', 'cv_jean.pdf', 2,false),

(2,10, 'Rabe', 'Marie', 27, 2, 'Toamasina', 'marie.rabe@email.com', 4,
 'Expérimentée et dynamique', 'cv_marie.pdf', 3,false),

(3,12, 'Randria', 'Paul', 30, 1, 'Fianarantsoa', 'paul.randria@email.com', 6,
 'Prêt à relever des défis', 'cv_paul.pdf', 4,false);

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
(1, 'Quels sont les avantages principaux de l''utilisation d''un modèle MVC dans le développement web ?'),
(1, 'Quelles sont les techniques utilisées pour prévenir les attaques par injection SQL ?'),
(1, 'Quels protocoles sont utilisés pour sécuriser les communications sur Internet ?'),
(1, 'Quels sont les principes fondamentaux de l''agilité selon le Manifeste Agile ?'),
(1, 'Quels sont les types de bases de données relationnelles ?'),
(1, 'Quels sont les avantages de l''utilisation des conteneurs comme Docker ?'),
(1, 'Quels sont les objectifs principaux de la méthode DevOps ?'),
(1, 'Quels langages sont couramment utilisés pour le développement web côté client ?'),
(1, 'Quels sont les risques associés à une mauvaise gestion des dépendances dans un projet logiciel ?'),
(1, 'Quelles sont les étapes clés du cycle de vie du développement logiciel (SDLC) ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(1, 'Réduction de la taille du code', FALSE),
(1, 'Séparation des préoccupations (logique, interface, données)', TRUE),
(1, 'Augmentation de la vitesse d''exécution', FALSE),
(1, 'Meilleure modularité et maintenance', TRUE),
(2, 'Utilisation de pare-feu', FALSE),
(2, 'Requêtes paramétrées', TRUE),
(2, 'Validation des entrées utilisateur', TRUE),
(2, 'Augmentation de la bande passante', FALSE),
(3, 'FTP', FALSE),
(3, 'HTTPS', TRUE),
(3, 'SSH', TRUE),
(3, 'SMTP', FALSE),
(4, 'Collaboration et communication continues', TRUE),
(4, 'Automatisation des tests', TRUE),
(4, 'Optimisation des profits', FALSE),
(4, 'Réduction des effectifs', FALSE),
(5, 'MySQL', TRUE),
(5, 'MongoDB', FALSE),
(5, 'PostgreSQL', TRUE),
(5, 'Redis', FALSE),
(6, 'Portabilité des applications', TRUE),
(6, 'Augmentation des coûts matériels', FALSE),
(6, 'Isolation des environnements', TRUE),
(6, 'Complexité accrue du code', FALSE),
(7, 'Amélioration de la collaboration entre développement et opérations', TRUE),
(7, 'Réduction du temps de mise sur le marché', TRUE),
(7, 'Augmentation des coûts de maintenance', FALSE),
(7, 'Centralisation des données', FALSE),
(8, 'JavaScript', TRUE),
(8, 'Python', FALSE),
(8, 'HTML/CSS', TRUE),
(8, 'SQL', FALSE),
(9, 'Conflits de versions', TRUE),
(9, 'Augmentation de la sécurité', FALSE),
(9, 'Retards dans les déploiements', TRUE),
(9, 'Réduction des coûts', FALSE),
(10, 'Analyse des besoins', TRUE),
(10, 'Conception', TRUE),
(10, 'Marketing du produit', FALSE),
(10, 'Maintenance', TRUE);

-- QCM pour Commercial
INSERT INTO qcm_questions (entity_id, question_text) VALUES
(2, 'Quels sont les éléments clés d''une stratégie de vente consultative ?'),
(2, 'Quels outils sont essentiels pour la gestion des relations client ?'),
(2, 'Quels indicateurs mesurent la performance commerciale ?'),
(2, 'Quels sont les avantages d''une segmentation de marché ?'),
(2, 'Quelles sont les étapes du processus de vente ?'),
(2, 'Quels sont les types de canaux de distribution ?'),
(2, 'Quelles sont les techniques de négociation efficaces ?'),
(2, 'Quels sont les objectifs d''une campagne de prospection ?'),
(2, 'Quels facteurs influencent la fidélité des clients ?'),
(2, 'Quels sont les avantages de l''analyse des données commerciales ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(11, 'Identifier les besoins du client', TRUE),
(11, 'Proposer des solutions adaptées', TRUE),
(11, 'Vendre le produit le moins cher', FALSE),
(11, 'Automatiser les ventes', FALSE),
(12, 'CRM (Customer Relationship Management)', TRUE),
(12, 'ERP (Enterprise Resource Planning)', FALSE),
(12, 'Logiciels de comptabilité', FALSE),
(12, 'Outils d''automatisation marketing', TRUE),
(13, 'NPS (Net Promoter Score)', TRUE),
(13, 'ROI (Return on Investment)', TRUE),
(13, 'Taux de conversion', TRUE),
(13, 'Nombre d''employés', FALSE),
(14, 'Meilleure compréhension des clients', TRUE),
(14, 'Réduction des coûts publicitaires', FALSE),
(14, 'Ciblage plus précis', TRUE),
(14, 'Augmentation des effectifs', FALSE),
(15, 'Prospection', TRUE),
(15, 'Négociation', TRUE),
(15, 'Clôture', TRUE),
(15, 'Production', FALSE),
(16, 'Vente directe', TRUE),
(16, 'Vente en ligne', TRUE),
(16, 'Vente par correspondance', TRUE),
(16, 'Vente interne', FALSE),
(17, 'Écoute active', TRUE),
(17, 'Proposition de valeur claire', TRUE),
(17, 'Augmentation des prix', FALSE),
(17, 'Flexibilité', TRUE),
(18, 'Augmenter le chiffre d''affaires', TRUE),
(18, 'Identifier de nouveaux clients', TRUE),
(18, 'Réduire les coûts', FALSE),
(18, 'Améliorer la logistique', FALSE),
(19, 'Qualité du service', TRUE),
(19, 'Expérience client', TRUE),
(19, 'Prix compétitifs', TRUE),
(19, 'Taille de l''entreprise', FALSE),
(20, 'Prise de décision éclairée', TRUE),
(20, 'Optimisation des campagnes', TRUE),
(20, 'Réduction des effectifs', FALSE),
(20, 'Augmentation des bénéfices', TRUE);

-- QCM pour RH
INSERT INTO qcm_questions (entity_id, question_text) VALUES
(3, 'Quels sont les objectifs de la Gestion Prévisionnelle des Emplois et des Compétences (GPEC) ?'),
(3, 'Quels documents sont obligatoires lors de l''embauche en France ?'),
(3, 'Quels sont les avantages d''un plan de formation continue ?'),
(3, 'Quelles sont les composantes d''une politique de rémunération ?'),
(3, 'Quels sont les indicateurs clés de performance RH ?'),
(3, 'Quelles sont les étapes du processus de recrutement ?'),
(3, 'Quels sont les risques liés à une mauvaise gestion des conflits en entreprise ?'),
(3, 'Quels sont les avantages de la diversité en milieu de travail ?'),
(3, 'Quelles lois régissent la protection des données personnelles des employés en Europe ?'),
(3, 'Quels sont les objectifs d''une évaluation annuelle des performances ?');

INSERT INTO qcm_options (question_id, option_text, is_correct) VALUES
(21, 'Anticiper les besoins en compétences', TRUE),
(21, 'Réduire les effectifs', FALSE),
(21, 'Planifier les évolutions de carrière', TRUE),
(21, 'Automatiser les processus RH', FALSE),
(22, 'Contrat de travail', TRUE),
(22, 'Attestation de formation', FALSE),
(22, 'Déclaration préalable à l''embauche', TRUE),
(22, 'CV', FALSE),
(23, 'Amélioration des compétences', TRUE),
(23, 'Augmentation de la productivité', TRUE),
(23, 'Réduction des congés', FALSE),
(23, 'Engagement des employés', TRUE),
(24, 'Salaire de base', TRUE),
(24, 'Avantages en nature', TRUE),
(24, 'Primes', TRUE),
(24, 'Réduction des coûts', FALSE),
(25, 'Taux de rétention des employés', TRUE),
(25, 'Taux d''absentéisme', TRUE),
(25, 'Chiffre d''affaires', FALSE),
(25, 'Satisfaction des employés', TRUE),
(26, 'Rédaction de l''offre d''emploi', TRUE),
(26, 'Sélection des candidats', TRUE),
(26, 'Intégration', TRUE),
(26, 'Marketing produit', FALSE),
(27, 'Baisse de la productivité', TRUE),
(27, 'Démotivation des équipes', TRUE),
(27, 'Augmentation des ventes', FALSE),
(27, 'Turnover élevé', TRUE),
(28, 'Innovation accrue', TRUE),
(28, 'Meilleure prise de décision', TRUE),
(28, 'Réduction des coûts', FALSE),
(28, 'Satisfaction des clients', TRUE),
(29, 'RGPD', TRUE),
(29, 'Code du travail', TRUE),
(29, 'Loi sur la cybersécurité', FALSE),
(29, 'Directive e-Privacy', TRUE),
(30, 'Identifier les forces et faiblesses', TRUE),
(30, 'Fixer des objectifs', TRUE),
(30, 'Réduire les salaires', FALSE),
(30, 'Planifier les promotions', TRUE);

-- QCM pour Finance
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
(32, 'Marge brute', TRUE),
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

-- QCM pour Marketing
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
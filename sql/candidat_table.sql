-- Script SQL pour créer la table candidat (PostgreSQL)
-- Supprimer la table si elle existe
DROP TABLE IF EXISTS candidat CASCADE;

-- Créer la table candidat avec la bonne structure
CREATE TABLE candidat (
    id SERIAL PRIMARY KEY,
    id_annonce INTEGER NOT NULL REFERENCES annonce(id) ON DELETE CASCADE,
    nom VARCHAR(40) NOT NULL,
    prenom VARCHAR(40) NOT NULL,
    age INTEGER NOT NULL CHECK (age >= 16 AND age <= 70),
    genre INTEGER NOT NULL CHECK (genre IN (1, 2)),
    adresse VARCHAR(60),
    email VARCHAR(40) NOT NULL,
    annees_experience INTEGER NOT NULL CHECK (annees_experience >= 0),
    lettre_motivation VARCHAR(300),
    cv VARCHAR(60),
    date_candidature DATE DEFAULT CURRENT_DATE
);

-- Index pour améliorer les performances
CREATE INDEX IF NOT EXISTS idx_candidat_annonce ON candidat(id_annonce);
CREATE INDEX IF NOT EXISTS idx_candidat_email ON candidat(email);

-- Données de test
INSERT INTO candidat (id_annonce, nom, prenom, age, genre, adresse, email, annees_experience, lettre_motivation, cv, date_candidature) VALUES
(1, 'Rakoto', 'Jean', 28, 1, '123 Rue Andriantany, Antananarivo', 'jean.rakoto@email.com', 3, 'Je suis très motivé pour ce poste car j''ai une solide expérience en développement Java et Spring.', 'cv_jean_rakoto.pdf', '2024-09-15'),
(1, 'Rasoa', 'Marie', 25, 2, '456 Avenue de l''Indépendance, Antananarivo', 'marie.rasoa@email.com', 2, 'Ma passion pour le développement web et mon expérience en React font de moi la candidate idéale.', 'cv_marie_rasoa.pdf', '2024-09-16'),
(2, 'Andry', 'Paul', 30, 1, '789 Rue Rainitovo, Fianarantsoa', 'paul.andry@email.com', 5, 'Fort de mes 5 années d''expérience en gestion de projet, je souhaite contribuer à vos équipes.', 'cv_paul_andry.pdf', '2024-09-17')
ON CONFLICT DO NOTHING;

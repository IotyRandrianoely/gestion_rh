-- Script de test pour vérifier la base de données
-- Exécutez ce script dans votre base PostgreSQL

-- 1. Vérifier que la table annonce existe
SELECT COUNT(*) as nombre_annonces FROM annonce;

-- 2. Lister les annonces disponibles
SELECT id, profil, description FROM annonce LIMIT 5;

-- 3. Vérifier si la table candidat existe
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public' 
AND table_name = 'candidat';

-- 4. Vérifier la structure de la table candidat si elle existe
\d candidat

-- 5. Si la table candidat n'existe pas, la créer
-- (Exécutez candidat_table.sql d'abord)

-- 6. Test d'insertion simple
INSERT INTO candidat (id_annonce, nom, prenom, age, genre, email, annees_experience) 
VALUES (1, 'Test', 'User', 25, 1, 'test@test.com', 2) 
ON CONFLICT DO NOTHING;

-- 7. Vérifier l'insertion
SELECT * FROM candidat WHERE email = 'test@test.com';

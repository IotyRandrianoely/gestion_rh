-- Ajouter la colonne etat avec une contrainte de validation
ALTER TABLE planing_entretien ADD COLUMN etat SMALLINT DEFAULT 0;
ALTER TABLE planing_entretien ADD CONSTRAINT planing_entretien_etat_check CHECK (etat IN (0, 1, 2));

-- Ajouter un commentaire pour expliquer les états
COMMENT ON COLUMN planing_entretien.etat IS '0=proposé, 1=fait, 2=annulé';
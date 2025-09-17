create table contrat_essai (
    id SERIAL PRIMARY KEY,
    id_candidat INT,
    id_poste INT,
    date_debut DATE,
    duree INT, -- en jours
    date_fin DATE,
    salaire NUMERIC(10,2),
    conditions TEXT,
    etat VARCHAR(20) default 'En attente',
    foreign key (id_candidat) references candidat(id),
    foreign key (id_poste) references poste(id)
);
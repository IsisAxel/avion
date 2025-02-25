-- Données pour la table type_siege
INSERT INTO type_siege (type) VALUES 
('BUSINESS'),
('ECONOMIQUE');

-- Données pour la table avion
INSERT INTO avion (compagnie, modele, date_fabrication, nombre_max_passager) VALUES 
('Air France', 'Boeing 777', '2010-05-15', 300),
('Delta Airlines', 'Airbus A320', '2015-08-20', 180);

-- Données pour la table siege_avion
INSERT INTO siege_avion (id_type_siege, id_avion, nombre_place) VALUES 
(1, 1, 50),  -- BUSINESS seats in Air France Boeing 777
(2, 1, 250), -- ECONOMIQUE seats in Air France Boeing 777
(1, 2, 20),  -- BUSINESS seats in Delta Airlines Airbus A320
(2, 2, 160); -- ECONOMIQUE seats in Delta Airlines Airbus A320

-- Données pour la table ville
INSERT INTO ville (nom, pays, imageUrl) VALUES 
('Paris', 'France', 'https://example.com/paris.jpg'),
('New York', 'USA', 'https://example.com/newyork.jpg'),
('Tokyo', 'Japan', 'https://example.com/tokyo.jpg'),
('London', 'UK', 'https://example.com/london.jpg');

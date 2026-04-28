CREATE DATABASE livrosdb;

CREATE TABLE IF NOT EXISTS livro (
    id BIGINT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    ano_publicacao INTEGER NOT NULL,
    categoria VARCHAR(100) NOT NULL
);

INSERT INTO livro (id, titulo, autor, ano_publicacao, categoria) VALUES
(1, 'Dom Casmurro', 'Machado de Assis', 1899, 'Romance'),
(2, 'Memorias Postumas de Bras Cubas', 'Machado de Assis', 1881, 'Romance'),
(3, 'Grande Sertao: Veredas', 'Joao Guimaraes Rosa', 1956, 'Ficcao'),
(4, 'A Hora da Estrela', 'Clarice Lispector', 1977, 'Romance'),
(5, 'Capitaes da Areia', 'Jorge Amado', 1937, 'Romance')
ON CONFLICT (id) DO NOTHING;

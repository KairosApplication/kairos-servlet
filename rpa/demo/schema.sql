-- Fixture exclusiva do MVP Servlet; nao usar schema do kairos-data/Spring.
BEGIN;
DO $$
BEGIN
    IF current_database() <> 'kairos_servlet_demo' THEN
        RAISE EXCEPTION 'Fixture exige kairos_servlet_demo vazio';
    END IF;
END $$;
CREATE TABLE admins (
    id serial PRIMARY KEY, email varchar(255) NOT NULL UNIQUE, senha varchar(255) NOT NULL
);
CREATE TABLE produtos (
    id serial PRIMARY KEY, marca varchar(100) NOT NULL, nome varchar(150) NOT NULL,
    quantidade_estoque integer NOT NULL CHECK (quantidade_estoque >= 0)
);
CREATE TABLE compras (id serial PRIMARY KEY, data_compra timestamp NOT NULL);
CREATE TABLE compras_produtos (
    id serial PRIMARY KEY, produto_id integer NOT NULL REFERENCES produtos(id),
    compra_id integer NOT NULL REFERENCES compras(id),
    quantidade_item integer NOT NULL CHECK (quantidade_item > 0),
    UNIQUE (compra_id, produto_id)
);
CREATE TABLE historico_compras_produtos (
    id serial PRIMARY KEY, compra_produto_id integer NOT NULL UNIQUE REFERENCES compras_produtos(id),
    id_produto integer NOT NULL, produto_marca varchar(100), produto_nome varchar(150)
);
-- LoginService existente compara senha diretamente: somente credencial ficticia local.
INSERT INTO admins(email, senha) VALUES (:'demo_email', :'demo_password');
INSERT INTO produtos(marca, nome, quantidade_estoque)
VALUES ('Demo', 'Arroz ficticio', 100), ('Demo', 'Feijao ficticio', 100);
COMMIT;

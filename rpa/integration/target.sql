-- Minimal fictional reference data, applied only to a NEW dedicated local volume.
BEGIN;
DO $$ BEGIN
    IF current_database() <> 'kairos_normalized_demo' THEN
        RAISE EXCEPTION 'Exige destino descartavel kairos_normalized_demo';
    END IF;
END $$;
CREATE SCHEMA rpa_integration;
CREATE TABLE rpa_integration.environment (
    singleton boolean PRIMARY KEY CHECK (singleton),
    purpose text NOT NULL CHECK (purpose = 'kairos-interdisciplinary-local-demo'),
    instance_id uuid NOT NULL DEFAULT gen_random_uuid(),
    original_schema_sha256 text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT current_timestamp
);
INSERT INTO rpa_integration.environment(singleton, purpose, original_schema_sha256)
VALUES (true, 'kairos-interdisciplinary-local-demo', :'schema_sha');

INSERT INTO Companies(id,name,cnpj,email,plan) VALUES
 (101,'EMPRESA FICTICIA DEMO','00000000000101','company-demo@kairos.invalid','BASIC'),
 (202,'OUTRA EMPRESA FICTICIA','00000000000202','other-demo@kairos.invalid','BASIC');
INSERT INTO Users(id,name,last_name,birth_date,cpf,email,password) VALUES
 (1001,'Cliente','FICTICIO DEMO','2000-01-01','00000001001','customer-demo@kairos.invalid','DEMO-NO-LOGIN'),
 (1002,'Outro','FICTICIO DEMO','2000-01-01','00000001002','other-customer@kairos.invalid','DEMO-NO-LOGIN');
INSERT INTO Customers(id,users_id,company_id) VALUES (501,1001,101),(502,1002,202);
INSERT INTO Products(id,company_id,brand,price,name) VALUES
 (10001,101,'Demo',10::numeric::money,'Arroz demonstracao'),
 (10002,101,'Demo',20::numeric::money,'Feijao demonstracao'),
 (20001,202,'Demo',30::numeric::money,'Produto outra empresa');
-- Different sequence ranges make the absence of ID copying visible in the demo.
SELECT setval(pg_get_serial_sequence('purchases','id'),10000,true);
SELECT setval(pg_get_serial_sequence('purchase_products','id'),20000,true);
SELECT setval(pg_get_serial_sequence('companies','id'),202,true);
SELECT setval(pg_get_serial_sequence('users','id'),1002,true);
SELECT setval(pg_get_serial_sequence('customers','id'),502,true);
SELECT setval(pg_get_serial_sequence('products','id'),20001,true);

CREATE TABLE rpa_integration.purchases (
    source_instance uuid NOT NULL,
    legacy_purchase_id integer NOT NULL,
    purchase_id integer NOT NULL UNIQUE REFERENCES Purchases(id),
    source_hash text NOT NULL,
    policy_hash text NOT NULL,
    source_snapshot jsonb NOT NULL,
    target_snapshot jsonb NOT NULL,
    item_count integer NOT NULL CHECK(item_count > 0),
    price_origin text NOT NULL CHECK(price_origin = 'current_demo_price'),
    synced_at timestamptz NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY(source_instance,legacy_purchase_id)
);
CREATE TABLE rpa_integration.attempts (
    id bigserial PRIMARY KEY, run_id uuid NOT NULL, source_instance uuid NOT NULL,
    legacy_purchase_id integer NOT NULL,
    purchase_id integer REFERENCES Purchases(id),
    occurred_at timestamptz NOT NULL DEFAULT current_timestamp,
    status text NOT NULL CHECK(status IN ('imported','already_imported','rejected')),
    item_count integer NOT NULL, reason text
);
COMMIT;

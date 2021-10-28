USE quarkus_test;
CREATE TABLE fruits (id SERIAL PRIMARY KEY, name TEXT NOT NULL);
INSERT INTO fruits (name) VALUES ('Kiwi');
INSERT INTO fruits (name) VALUES ('Durian');
INSERT INTO fruits (name) VALUES ('Pomelo');
INSERT INTO fruits (name) VALUES ('Lychee');
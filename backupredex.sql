SET FOREIGN_KEY_CHECKS = 0; 
SET @tables = NULL;
SELECT GROUP_CONCAT(table_schema, '.', table_name) INTO @tables
  FROM information_schema.tables 
  WHERE table_schema = 'heroku_4d36c97a7276574'; -- specify DB name here.

SET @tables = CONCAT('DROP TABLE ', @tables);

PREPARE stmt FROM @tables;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SET FOREIGN_KEY_CHECKS = 1; 


use heroku_4d36c97a7276574;

  
alter table continente AUTO_INCREMENT = 1;  
alter table pais AUTO_INCREMENT = 1;
alter table vuelo AUTO_INCREMENT = 1;
alter table persona AUTO_INCREMENT = 1;
alter table planvuelo AUTO_INCREMENT = 1;
alter table colaborador AUTO_INCREMENT = 1;
alter table usuario AUTO_INCREMENT = 1;
alter table rol AUTO_INCREMENT = 1;
alter table documento_identidad AUTO_INCREMENT = 1;

/* PAISES */
insert into continente(nombre) values ("África"), ("America"), ("Asia"), ("Europa"), ("Oceania");

INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Argentina", "SABE");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Bolivia", "SLLP");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Brasil", "SBBR");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Chile", "SCEL");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Colombia", "SKBO");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Ecuador", "SEQM");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Paraguay", "SGAS");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Perú", "SPIM");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Uruguay", "SUAA");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (2, "Venezuela", "SVMI");

INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Albania", "LATI");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Alemania", "EDDI");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Austria", "LOWW");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Belgica", "EBCI");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Bielorrusia", "UMMS");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Bulgaria", "LBSF");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Checa", "LKPR");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Croacia", "LDZA");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Dinamarca", "EKCH");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Eslovaquia", "LZIB");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Eslovenia", "LJLJ");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "España", "LEMD");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Estonia", "EETN");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Finlandia", "EFHK");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Francia", "LFPG");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Grecia", "LGAV");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Holanda", "EHAM");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Hungría", "LHBP");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Irlanda", "EIDW");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Islandia", "BIKF");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Italia", "LIRA");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Letonia", "EVRA");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Luxemburgo", "ELLX");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Malta", "LMML");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Noruega", "ENGM");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Polonia", "EPMO");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Portugal", "LPPT");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Reino Unido", "EGLL");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Suecia", "ESKN");
INSERT INTO pais(id_continente, nombre, codigo) VALUES (4, "Suiza", "LSZB");


/* ROLES */
insert into rol(id, codigo, nombre) values
  (1, 'ADMIN', 'Administrador'),
  (2, 'GERENTE_GENERAL', 'Gerente general'),
  (3, 'JEFE_OFICINA', 'Jefe de oficina'),
  (4, 'EMPLEADO', 'Empleado');


/* DOCUMENTOS DE IDENTIDAD */
insert into documento_identidad(id, nombre, simbolo, id_pais) VALUES
  (1, 'Documento nacional de identidad', 'DNI', 8);
  

  
  
  SELECT * FROM CONTINENTE;
DROP DATABASE IF EXISTS Doodo;
CREATE DATABASE Doodo;
USE Doodo;

CREATE TABLE cat_regimen (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regimen varchar(100) NOT NULL UNIQUE
);

CREATE TABLE cat_CFDI (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cfdi varchar(150) NOT NULL UNIQUE
);

CREATE TABLE cliente (
    rfc varchar(13) NOT NULL PRIMARY KEY,
    pais_origen varchar(50) NOT NULL,
    nombre_razon_social varchar(100) NOT NULL UNIQUE,
    direccion text NOT NULL,
    /*
    CALLE 
    numero ext
    numero interior nullable
    entidad federativa
    municipio
    colonia
    cp 
    */
    regimen_id INT NOT NULL,
    cfdi_id INT NOT NULL,
    FOREIGN KEY (regimen_id) REFERENCES cat_regimen(id),
    FOREIGN KEY (cfdi_id) REFERENCES cat_CFDI(id)
);

CREATE TABLE factura (
	id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
    rfc varchar(13) NOT NULL,
    mail varchar(50) NOT NULL,
    factura text NOT NULL,
    FOREIGN KEY (rfc) REFERENCES cliente(rfc)
);
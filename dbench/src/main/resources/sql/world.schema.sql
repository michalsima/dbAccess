create schema DBENCH;

use DBENCH;

CREATE TABLE IF NOT EXISTS country (
  Code CHAR(3) NOT NULL,
  Name CHAR(52) NOT NULL,
  Continent varchar(255) NOT NULL,
  Region CHAR(26) NOT NULL,
  SurfaceArea DECIMAL(10,2) NOT NULL,
  IndepYear SMALLINT(6),
  Population INT(11) NOT NULL,
  LifeExpectancy DECIMAL(3,1),
  GNP DECIMAL(10,2),
  GNPOld DECIMAL(10,2),
  LocalName CHAR(45) NOT NULL,
  GovernmentForm CHAR(45) NOT NULL,
  HeadOfState CHAR(60),
  Capital INT(11),
  Code2 CHAR(2) NOT NULL,
  PRIMARY KEY (Code)
);

CREATE TABLE IF NOT EXISTS city (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  Name CHAR(35) NOT NULL,
  CountryCode CHAR(3) NOT NULL,
  District CHAR(25) NOT NULL,
  Population INT(11) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT city_ibfk_1 FOREIGN KEY (CountryCode) REFERENCES country (Code)
)AUTO_INCREMENT=4080;


CREATE TABLE IF NOT EXISTS countrylanguage (
  CountryCode CHAR(3) NOT NULL,
  Language CHAR(30) NOT NULL,
  IsOfficial varchar(255) NOT NULL,
  Percentage DECIMAL(4,1) NOT NULL,
  PRIMARY KEY (CountryCode,Language),
  CONSTRAINT countryLanguage_ibfk_1 FOREIGN KEY (CountryCode) REFERENCES country (Code)
);

CREATE INDEX city_cc ON DBENCH.CITY(CountryCode);
CREATE INDEX city_population ON DBENCH.CITY(Population);

CREATE INDEX country_capital ON DBENCH.COUNTRY(Capital);
CREATE INDEX country_surface ON DBENCH.COUNTRY(SurfaceArea);
CREATE INDEX country_population ON DBENCH.COUNTRY(Population);
CREATE INDEX country_region ON DBENCH.COUNTRY(Region);

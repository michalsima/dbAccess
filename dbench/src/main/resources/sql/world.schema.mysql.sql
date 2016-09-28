USE `dbench`;


DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` char(35) NOT NULL,
  `CountryCode` char(3) NOT NULL,
  `District` char(25) NOT NULL,
  `Population` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `city_ibfk_1` (`CountryCode`),
  KEY `population` (`Population`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`CountryCode`) REFERENCES `country` (`Code`)
) ENGINE=InnoDB AUTO_INCREMENT=4080 DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `Code` char(3) NOT NULL,
  `Name` char(52) NOT NULL,
  `Continent` varchar(255) NOT NULL,
  `Region` char(26) NOT NULL,
  `SurfaceArea` decimal(10,2) NOT NULL,
  `IndepYear` smallint(6) DEFAULT NULL,
  `Population` int(11) NOT NULL,
  `LifeExpectancy` decimal(3,1) DEFAULT NULL,
  `GNP` decimal(10,2) DEFAULT NULL,
  `GNPOld` decimal(10,2) DEFAULT NULL,
  `LocalName` char(45) NOT NULL,
  `GovernmentForm` char(45) NOT NULL,
  `HeadOfState` char(60) DEFAULT NULL,
  `Capital` int(11) DEFAULT NULL,
  `Code2` char(2) NOT NULL,
  PRIMARY KEY (`Code`),
  KEY `capital_idx` (`Capital`),
  KEY `surface` (`SurfaceArea`),
  KEY `population` (`Population`),
  KEY `region` (`Region`),
  CONSTRAINT `capital` FOREIGN KEY (`Capital`) REFERENCES `city` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `countrylanguage`
--

DROP TABLE IF EXISTS `countrylanguage`;
CREATE TABLE `countrylanguage` (
  `CountryCode` char(3) NOT NULL,
  `Language` char(30) NOT NULL,
  `IsOfficial` varchar(255) NOT NULL,
  `Percentage` decimal(4,1) NOT NULL,
  PRIMARY KEY (`CountryCode`,`Language`),
  CONSTRAINT `countryLanguage_ibfk_1` FOREIGN KEY (`CountryCode`) REFERENCES `country` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
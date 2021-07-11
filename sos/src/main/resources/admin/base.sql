---- --------------------------------------------------------
--
-- Table structure for table `deparments`
--
CREATE TABLE `deparments` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `name` varchar(45) NOT NULL,
  `lat` DOUBLE NOT NULL DEFAULT '0',
  `lon` DOUBLE NOT NULL DEFAULT '0',
  `role` enum('police', 'fire_station', 'ambulence', 'samu') COLLATE utf8_bin NOT NULL DEFAULT 'police'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
---- --------------------------------------------------------
--
-- Table structure for table `users`
--
CREATE TABLE `users` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `address` varchar(45) NULL,
  `city` varchar(45) NULL,
  `state` varchar(45) NULL,
  `zipcode` varchar(45) NULL,
  `phone` varchar(45) NULL,
  `skype` varchar(45) NULL,
  `email` varchar(45) NOT NULL,
  `pwd` varchar(45) NOT NULL,
  `birthDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
---- --------------------------------------------------------
--
-- Table structure for table `health_card`
--
CREATE TABLE `health_card` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `anaphylaxis` int(11) NOT NULL DEFAULT 0,
  `epipen` int(11) NOT NULL DEFAULT 0,
  `diabetes` int(11) NOT NULL DEFAULT 0,
  `organ_donor` int(11) NOT NULL DEFAULT 0,
  `family_doctor` int(11) NOT NULL DEFAULT 0,
  `blood_group` enum('A+', 'B+', 'AB+', 'O+', 'A-', 'B-', 'AB-', 'O-') COLLATE utf8_bin NOT NULL DEFAULT 'A+',
  `doctor` varchar(45) NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
---- --------------------------------------------------------
--
-- Table structure for table `urgence`
--
CREATE TABLE `urgence` (
  `id` int(11) NOT NULL  PRIMARY KEY AUTO_INCREMENT,
  `idUser` int(11) NOT NULL,
  `type` enum(
    'police',
    'fire_station',
    'ambulence',
    'samu',
    'all'
  ) COLLATE utf8_bin NOT NULL DEFAULT 'police' ,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lat` DOUBLE NOT NULL DEFAULT '0',
  `lon` DOUBLE NOT NULL DEFAULT '0',
  `response` int(11) NOT NULL DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
---- --------------------------------------------------------
--
-- Table structure for table `family_group`
--
CREATE TABLE `family_group` (
  `phone_number` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `name` varchar(45) NULL,
  PRIMARY KEY (`phone_number`, `idUser`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
---- --------------------------------------------------------
--
-- Table structure for table `urgence_message`
--
CREATE TABLE `urgence_message` (
  `idUser` int(11) NOT NULL  PRIMARY KEY,
  `message` varchar(255) NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
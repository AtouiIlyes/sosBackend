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
  `address` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zipcode` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `skype` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `pwd` varchar(45) NOT NULL,
  `birthDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
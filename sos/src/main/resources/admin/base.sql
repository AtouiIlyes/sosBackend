
---- --------------------------------------------------------
--
-- Table structure for table `zone`
--
CREATE TABLE `deparments` (
  `id` int(11) NOT NULL PRIMARY KEY,
  `name` varchar(45) NOT NULL,
  `lat` DOUBLE NOT NULL DEFAULT '0',
  `lon` DOUBLE NOT NULL DEFAULT '0',
  `role` enum('police','fire_station','ambulence','samu') COLLATE utf8_bin NOT NULL DEFAULT 'police'
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COLLATE = utf8_bin;
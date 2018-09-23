CREATE SCHEMA IF NOT EXISTS `iiht`;
COMMIT;

CREATE USER IF NOT EXISTS `iiht`@`localhost` 
IDENTIFIED BY 'tracker' 
REQUIRE NONE PASSWORD EXPIRE NEVER;
COMMIT;

GRANT ALTER, CREATE, DELETE, DROP, GRANT OPTION, INSERT, SELECT, UPDATE
ON iiht.* TO iiht;
COMMIT;

CREATE TABLE `user` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(20) NOT NULL,
  `lastname` varchar(20) NOT NULL,
  `midname` varchar(20) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(1000) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
COMMIT;

CREATE TABLE `category` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `userid` int(6) NOT NULL,
  `category` varchar(50) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `category_fk1` (`userid`),
  CONSTRAINT `category_fk1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
COMMIT;

CREATE TABLE `workout` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `userid` int(6) NOT NULL,
  `categoryid` int(9) NOT NULL,
  `title` varchar(128) NOT NULL,
  `notes` varchar(256) DEFAULT NULL,
  `burnrate` double DEFAULT '2',
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `deleted` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `workout_fk1` (`userid`),
  KEY `workout_fk2` (`categoryid`),
  CONSTRAINT `workout_fk1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`),
  CONSTRAINT `workout_fk2` FOREIGN KEY (`categoryid`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
COMMIT;

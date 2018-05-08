CREATE SCHEMA `ivmanager` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

USE ivmanager;

CREATE TABLE `t_modle` (
  `id` int(11) NOT NULL,
  `modle_name` varchar(100) DEFAULT NULL,
  `modle_action` varchar(100) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- MySQL dump 10.13  Distrib 5.7.19, for Linux (x86_64)
--
-- Host: localhost    Database: ivuser
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ivuser_check_code`
--

CREATE SCHEMA `ivuser` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use ivuser;

DROP TABLE IF EXISTS `ivuser_check_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_check_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(15) NOT NULL,
  `check_code` varchar(10) NOT NULL COMMENT '验证码',
  `type` smallint(2) DEFAULT NULL COMMENT '验证码类型，1-短信，2-邮件',
  `create_time` datetime NOT NULL,
  `expire_time` datetime NOT NULL COMMENT '验证码失效时间，根据type不同',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ivuser_check_code`
--

LOCK TABLES `ivuser_check_code` WRITE;
/*!40000 ALTER TABLE `ivuser_check_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `ivuser_check_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ivuser_loan_record`
--

DROP TABLE IF EXISTS `ivuser_loan_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_loan_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loan_id` int(10) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `amount` int(10) NOT NULL DEFAULT '0',
  `participation_amount` int(10) NOT NULL DEFAULT '0',
  `coupon_amount` double(5,0) NOT NULL DEFAULT '0',
  `coupon_status` smallint(2) NOT NULL DEFAULT '-1',
  `policy_id` bigint(20) NOT NULL,
  `policy_type` smallint(2) NOT NULL,
  `is_delete` bit(2) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ivuser_main_policy`
--

DROP TABLE IF EXISTS `ivuser_main_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_main_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `main_policy_id` bigint(20) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `status` smallint(2) NOT NULL DEFAULT '0' COMMENT '用户主策略状态，0-关闭，1-开启',
  `third_user_uuid` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `amount_start` int(8) NOT NULL DEFAULT '0' COMMENT '起步投标金额',
  `amount_max` int(8) NOT NULL DEFAULT '0' COMMENT '最大投标金额',
  `account_remain` int(8) NOT NULL DEFAULT '0' COMMENT '账户保留金额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_user_id_third_main_policy_id` (`main_policy_id`,`third_user_uuid`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ivuser_policy`
--

DROP TABLE IF EXISTS `ivuser_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `third_user_uuid` varchar(50) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `policy_id` bigint(20) NOT NULL,
  `bid_amount` int(8) NOT NULL COMMENT '投标金额，受主策略投资金额范围限制',
  `status` smallint(2) NOT NULL DEFAULT '0' COMMENT '1-开启，0-停止',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_userpolicy` (`user_id`,`third_user_uuid`,`is_delete`,`policy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ivuser_third_bind_info`
--

DROP TABLE IF EXISTS `ivuser_third_bind_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_third_bind_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `thrid_user_uuid` varchar(50) NOT NULL COMMENT '第三方用户名/id/手机号',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `expired_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_userId_thirdUserUUID` (`user_id`,`thrid_user_uuid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ivuser_user`
--

DROP TABLE IF EXISTS `ivuser_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ivuser_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(15) NOT NULL COMMENT '注册手机号',
  `password` varchar(100) NOT NULL COMMENT '加密密码',
  `referrer_mobile` varchar(15) DEFAULT NULL COMMENT '推荐人手机号',
  `nick` varchar(45) NOT NULL COMMENT '昵称',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `user_token` varchar(60) NOT NULL COMMENT '用户token md5',
  `security_key` varchar(60) NOT NULL COMMENT 'md5安全码，用于对密码加密',
  `register_source` varchar(10) NOT NULL,
  `head_img_org` longtext NOT NULL COMMENT '原图id',
  `head_img` longtext NOT NULL COMMENT '缩略图id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile_unique` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ivuser_user`
--

LOCK TABLES `ivuser_user` WRITE;
/*!40000 ALTER TABLE `ivuser_user` DISABLE KEYS */;
INSERT INTO `ivuser_user` VALUES (1,'18611410103','4721A25F05EE997CF9DAD40D2DA548B4',NULL,'拍客','\0','2017-09-04 09:00:30','2017-09-04 09:00:30','9B2A9E304129B511DFA5CA935CA587F1','4D88968F7319A6B08F0C48A05AE39E3D','3','',''),(2,'13313066864','F08E17482D5264C35D918BC504BCF136',NULL,'拍客','\0','2017-09-09 11:37:37','2017-09-09 11:37:37','D46BACBC67C31E4C53ECF6E9D99454E7','C9929AE53469CD07FD2878C62ABBCD04','3','',''),(3,'13622034018','BFBC9555749D1B3321D02B394CD37EBF',NULL,'拍客','\0','2017-09-09 13:42:20','2017-09-09 13:42:20','5D3E7F7AD9F9C405F363D3FE4AB45974','323DDE6DBC2F8355B87FB833BCDC9552','3','',''),(4,'13480808100','AC3306922B91D37B1BB52136D9B96042',NULL,'拍客','\0','2017-09-11 08:40:27','2017-09-11 08:40:27','0F8006CAD12B394851E2488858964C1B','8F40606CCC1978D2DB645AC89CD01025','3','','');
/*!40000 ALTER TABLE `ivuser_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_channel_url`
--

DROP TABLE IF EXISTS `security_channel_url`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_channel_url` (
  `id` bigint(20) NOT NULL,
  `channel` varchar(20) NOT NULL COMMENT '前端请求渠道枚举，见SourceEnum',
  `url_code` varchar(50) NOT NULL COMMENT 'url code 安全暴露',
  `url` varchar(100) NOT NULL COMMENT '请求uri 不带域名的path',
  `need_token_auth` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否需要token认证',
  `expired` bit(1) NOT NULL DEFAULT b'0' COMMENT '该url配置是否过期，1-过期',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_urlCode_unique` (`channel`,`url_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_biz_opt_log`
--

DROP TABLE IF EXISTS `t_biz_opt_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_biz_opt_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `biz_id` bigint(20) NOT NULL COMMENT '业务id，如系统散标策略id',
  `opt_type` tinyint(2) NOT NULL COMMENT '操作类型',
  `status` varchar(45) NOT NULL COMMENT '操作日志状态：根据各业务定义',
  `is_delete` bit(1) NOT NULL COMMENT '逻辑删除，如果物理删除后可物理清楚',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `biz_related_obj` varchar(45) DEFAULT NULL COMMENT '业务关联实体',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_loan_policy`
--

DROP TABLE IF EXISTS `t_loan_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_loan_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `risk_level` smallint(2) DEFAULT NULL COMMENT '风险等级,低中高',
  `policy_type` smallint(2) NOT NULL COMMENT '策略类型,1-系统散标,2-自定义散标',
  `valid_time` datetime NOT NULL,
  `name` varchar(100) NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'1' COMMENT '1-开启，0-停止',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `credit_code` bigint(20) DEFAULT '0' COMMENT '魔镜等级，二进制位 AAA=001,AA=010,A=100,B,C,D,E,F',
  `month_begin` int(2) DEFAULT '0' COMMENT '起始期限 月',
  `month_end` int(2) DEFAULT '0' COMMENT '截止期限 月',
  `rate_begin` int(2) DEFAULT '0',
  `rate_end` int(2) DEFAULT '0',
  `amount_begin` int(8) DEFAULT '0' COMMENT '借款 起始金额',
  `amount_end` int(8) DEFAULT '0' COMMENT '借款 截止金额',
  `age_begin` smallint(3) DEFAULT NULL,
  `age_end` smallint(3) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `third_auth_info` bigint(20) DEFAULT '0' COMMENT '第三方认证信息\n学历认证，征信认证，视频认证，户籍认证，手机认证，学籍认证',
  `certificate` bigint(20) DEFAULT '0' COMMENT '学历',
  `study_style` bigint(20) DEFAULT '0' COMMENT '学习形式,普通，自考，函授，网络教育',
  `graduate_school_type` bigint(20) DEFAULT '0' COMMENT '学校分类-985 211 一本 二本 三本 职高',
  `loaner_success_count_begin` smallint(5) DEFAULT '0' COMMENT '借款人成功借款次数，第一次借款，多次借款',
  `loaner_success_count_end` smallint(5) DEFAULT '0',
  `waste_count_begin` smallint(5) DEFAULT '0',
  `waste_count_end` smallint(5) DEFAULT '0',
  `normal_count_begin` smallint(5) DEFAULT '0',
  `normal_count_end` smallint(5) DEFAULT '0',
  `overdue_less_count_begin` smallint(5) DEFAULT '0',
  `overdue_less_count_end` smallint(5) DEFAULT '0',
  `overdue_more_count_begin` smallint(5) DEFAULT '0',
  `overdue_more_count_end` smallint(5) DEFAULT '0',
  `total_principal_begin` int(8) DEFAULT '0',
  `total_principal_end` int(8) DEFAULT '0',
  `owing_principal_begin` int(8) DEFAULT '0',
  `owing_principal_end` int(8) DEFAULT '0',
  `amount_to_receive_begin` int(8) DEFAULT '0',
  `amount_to_receive_end` int(8) DEFAULT '0',
  `last_success_borrowdays_begin` smallint(3) DEFAULT '0',
  `last_success_borrowdays_end` smallint(3) DEFAULT '0',
  `total_flag` bit(64) DEFAULT b'0' COMMENT '总打标值 用于一次匹配标的详情总打标值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_main_policy`
--

DROP TABLE IF EXISTS `t_main_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_main_policy` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `status` smallint(2) NOT NULL COMMENT '主策略状态 0-下架，1-上架',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_main_policy`
--

LOCK TABLES `t_main_policy` WRITE;
/*!40000 ALTER TABLE `t_main_policy` DISABLE KEYS */;
INSERT INTO `t_main_policy` VALUES (1,'系统散标',1,'\0','2017-08-15 09:37:55','2017-08-15 09:37:56'),(2,'自定义散标',1,'\0','2017-08-15 09:38:16','2017-08-15 09:38:18'),(3,'自定义债权',1,'\0','2017-08-15 09:40:43','2017-08-15 09:40:45'),(4,'跟踪',1,'\0','2017-08-15 09:41:01','2017-08-15 09:41:03');
/*!40000 ALTER TABLE `t_main_policy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-13  8:20:55


ALTER TABLE `ivuser`.`ivuser_user`
ADD COLUMN `last_login_time` DATETIME NULL AFTER `head_img`;

ALTER TABLE `ivuser`.`ivuser_third_bind_info`
ADD COLUMN `access_token` VARCHAR(100) NOT NULL AFTER `expired_time`,
ADD COLUMN `refresh_token` VARCHAR(100) NOT NULL AFTER `access_token`,
ADD COLUMN `open_id` VARCHAR(100) NOT NULL AFTER `refresh_token`;

ALTER TABLE `ivuser`.`t_loan_policy`
CHANGE COLUMN `rate_begin` `rate_begin` DOUBLE NULL DEFAULT '0' ,
CHANGE COLUMN `rate_end` `rate_end` DOUBLE NULL DEFAULT '0' ;

CREATE TABLE `t_notify_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) COLLATE utf8_bin NOT NULL,
  `content` varchar(256) COLLATE utf8_bin NOT NULL,
  `type` tinyint(2) NOT NULL COMMENT '1-活动,2-策略,3-安全',
  `status` tinyint(2) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `link` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `t_user_notify_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `status` tinyint(2) NOT NULL COMMENT '0-未读，1-已读',
  `message_id` bigint(20) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uniq_userId_mesaageId` (`user_id`,`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE `ivuser`.`t_notify_message`
ADD COLUMN `unique_key` VARCHAR(45) NULL AFTER `id`;

ALTER TABLE `ivuser`.`t_user_notify_message`
ADD COLUMN `message_value` VARCHAR(128) NULL AFTER `message_id`;

ALTER TABLE `ivuser`.`t_loan_policy`
ADD COLUMN `amount_owing_total_begin` INT(8) NULL AFTER `last_success_borrowdays_end`,
ADD COLUMN `amount_owing_total_end` INT(8) NULL AFTER `amount_owing_total_begin`;

ALTER TABLE `ivuser`.`t_loan_policy`
ADD COLUMN `owing_highest_debt_ratio_begin` DOUBLE NULL AFTER `total_flag`,
ADD COLUMN `owing_highest_debt_ratio_end` DOUBLE NULL AFTER `owing_highest_debt_ratio_begin`;

ALTER TABLE `ivuser`.`t_loan_policy`
CHANGE COLUMN `total_flag` `total_flag` BIT(64) NULL DEFAULT b'0' COMMENT '总打标值 用于一次匹配标的详情总打标值' AFTER `owing_highest_debt_ratio_end`;



CREATE TABLE `ivuser`.`t_user_account` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `zhuobao_balance` INT(8) NOT NULL DEFAULT 0 COMMENT '余额：',
  `cash_balance` INT(8) NOT NULL DEFAULT 0,
  `status` TINYINT(2) NOT NULL DEFAULT 0,
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `ivuser`.`t_user_account`
ADD COLUMN `user_id` BIGINT(20) NOT NULL AFTER `id`;

ALTER TABLE `ivuser`.`t_user_account`
ADD COLUMN `bid_amount_balance` INT(8) NOT NULL DEFAULT 0 COMMENT '剩余可投标余额' AFTER `cash_balance`;


ALTER TABLE `ivuser`.`t_loan_policy`
ADD COLUMN `register_borrow_months_begin` SMALLINT(4) NULL AFTER `owing_highest_debt_ratio_end`,
ADD COLUMN `register_borrow_months_end` SMALLINT(4) NULL AFTER `register_borrow_months_begin`;

ALTER TABLE `ivuser`.`t_loan_policy`
ADD COLUMN `amt_debt_rat_bg` DOUBLE NULL AFTER `register_borrow_months_end`,
ADD COLUMN `amt_debt_rat_ed` DOUBLE NULL AFTER `amt_debt_rat_bg`;

ALTER TABLE `ivuser`.`ivuser_loan_record`
ADD COLUMN `down_detail_flag` TINYINT(2) NOT NULL DEFAULT 0 AFTER `is_delete`;

CREATE TABLE `ivuser`.`t_loan_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `listing_id` INT(10) NOT NULL,
  `fist_bid_time` DATETIME NULL,
  `last_bid_time` DATETIME NULL,
  `lender_count` SMALLINT(5) NULL,
  `credit_code` VARCHAR(10) NULL,
  `amount` INT(8) NULL,
  `months` INT(4) NULL,
  `rate` DOUBLE NULL,
  `borrow_name` VARCHAR(45) NULL,
  `gender` TINYINT(2) NULL,
  `age` TINYINT(3) NULL,
  `education_degree` VARCHAR(45) NULL,
  `graduate_school` VARCHAR(100) NULL,
  `study_style` VARCHAR(45) NULL,
  `success_count` TINYINT(4) NULL,
  `waste_count` TINYINT(4) NULL,
  `cancel_count` TINYINT(4) NULL,
  `failed_count` TINYINT(4) NULL,
  `normal_count` TINYINT(4) NULL,
  `overdueless_count` TINYINT(4) NULL,
  `overduemore_count` TINYINT(4) NULL,
  `owing_principal` INT(8) NULL,
  `owing_amount` INT(8) NULL,
  `amount_to_receive` INT(8) NULL,
  `first_success_borrow_time` DATETIME NULL,
  `last_success_borrow_time` DATETIME NULL,
  `register_time` DATETIME NULL,
  `highest_principal` INT(8) NULL,
  `highest_debt` INT(8) NULL,
  `total_principal` INT(8) NULL,
  `thirdauth_flag` BIGINT(20) NULL,
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));
ALTER TABLE `ivuser`.`t_loan_detail`
CHANGE COLUMN `success_count` `success_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `waste_count` `waste_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `cancel_count` `cancel_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `failed_count` `failed_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `normal_count` `normal_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `overdueless_count` `overdueless_count` SMALLINT(4) NULL DEFAULT NULL ,
CHANGE COLUMN `overduemore_count` `overduemore_count` SMALLINT(4) NULL DEFAULT NULL ;

ALTER TABLE `ivuser`.`t_loan_detail`
ADD INDEX `index_listing_id` (`listing_id` ASC);


ALTER TABLE `ivuser`.`ivuser_loan_record`
ADD INDEX `index_create_time` (`create_time` ASC);
ALTER TABLE `ivuser`.`ivuser_loan_record`
ADD INDEX `index_loan_id` (`loan_id` ASC);

CREATE TABLE `ivuser`.`t_bid_analysis` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `month` SMALLINT(4) NULL,
  `bid_count_total` INT(5) NULL,
  `bid_amount_total` INT(8) NULL,
  `bid_rate_avg` DOUBLE NULL,
  `bid_month_avg` DOUBLE NULL,
  `bid_age_avg` DOUBLE NULL,
  `bid_lender_count_avg` DOUBLE NULL,
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));


  ALTER TABLE `ivuser`.`t_bid_analysis`
CHANGE COLUMN `month` `month` VARCHAR(15) NULL DEFAULT NULL ;

ALTER TABLE `ivuser`.`ivuser_loan_record`
ADD COLUMN `down_repayment_flag` TINYINT(2) NOT NULL DEFAULT 0 AFTER `down_detail_flag`;


CREATE TABLE `ivuser`.`t_loan_repayment_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `listing_id` INT(10) NOT NULL,
  `order_id` SMALLINT(2) NULL,
  `due_date` DATETIME NULL,
  `repay_date` DATETIME NULL,
  `repay_principal` DOUBLE NULL,
  `repay_interest` DOUBLE NULL,
  `owing_principal` DOUBLE NULL,
  `owing_interest` DOUBLE NULL,
  `owing_overdue` DOUBLE NULL,
  `overdue_days` SMALLINT(5) NULL,
  `repay_status` SMALLINT(2) NULL,
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));


ALTER TABLE `ivuser`.`t_loan_repayment_detail`
ADD COLUMN `user_id` BIGINT(20) NOT NULL AFTER `id`,
ADD COLUMN `username` VARCHAR(45) NOT NULL AFTER `user_id`;

ALTER TABLE `ivuser`.`t_loan_detail`
DROP INDEX `index_listing_id` ,
ADD UNIQUE INDEX `index_listing_id` (`listing_id` ASC);


ALTER TABLE `ivuser`.`t_bid_analysis`
ADD COLUMN `overdue_10_days` INT(5) NULL DEFAULT 0 AFTER `bid_lender_count_avg`,
ADD COLUMN `overdue_30_days` INT(5) NULL DEFAULT 0 AFTER `overdue_10_days`,
ADD COLUMN `overdue_60_days` INT(5) NULL DEFAULT 0 AFTER `overdue_30_days`,
ADD COLUMN `overdue_90_days` INT(5) NULL DEFAULT 0 AFTER `overdue_60_days`;


ALTER TABLE `ivuser`.`t_bid_analysis`
CHANGE COLUMN `bid_count_total` `bid_count_total` INT(5) NULL DEFAULT 0 ,
CHANGE COLUMN `bid_amount_total` `bid_amount_total` INT(8) NULL DEFAULT 0 ,
CHANGE COLUMN `bid_rate_avg` `bid_rate_avg` DOUBLE NULL DEFAULT 0 ,
CHANGE COLUMN `bid_month_avg` `bid_month_avg` DOUBLE NULL DEFAULT 0 ,
CHANGE COLUMN `bid_age_avg` `bid_age_avg` DOUBLE NULL DEFAULT 0 ,
CHANGE COLUMN `bid_lender_count_avg` `bid_lender_count_avg` DOUBLE NULL DEFAULT 0 ;


ALTER TABLE `ivuser`.`t_bid_analysis`
ADD UNIQUE INDEX `unique_key` (`user_id` ASC, `username` ASC, `month` ASC);


CREATE TABLE `ivuser`.`t_blacklist_third` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `is_delete` BIT(1) NOT NULL COMMENT '1-ppd用户，2-其他',
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `ivuser`.`t_blacklist_third`
CHANGE COLUMN `is_delete` `is_delete` BIT(1) NOT NULL DEFAULT 0 COMMENT '1-ppd用户，2-其他' ;


ALTER TABLE `ivuser`.`t_blacklist_third`
DROP COLUMN `user_id`,
CHANGE COLUMN `username` `blacklist_value` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
ADD COLUMN `blacklist_type` VARCHAR(45) NULL COMMENT '1-第三方账户username，2-用户id' AFTER `update_time`;


ALTER TABLE `ivuser`.`t_blacklist_third`
ADD UNIQUE INDEX `unique_key` (`blacklist_value` ASC, `blacklist_type` ASC);

ALTER TABLE `ivuser`.`t_bid_analysis`
ADD COLUMN `overdue_principal` DOUBLE NULL DEFAULT 0 AFTER `overdue_90_days`;

CREATE TABLE `ivuser`.`t_loan_overdue_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `listing_id` INT(10) NOT NULL,
  `overdue_type` TINYINT(2) NOT NULL COMMENT '1-逾期10天，2-逾期30天，3-逾期60天，4-逾期90天',
  `start_interest_date` DATETIME NOT NULL COMMENT '起息日期',
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `ivuser`.`t_loan_overdue_detail`
ADD COLUMN `repayment_detail_id` BIGINT(20) NOT NULL AFTER `start_interest_date`;


ALTER TABLE `ivuser`.`t_bid_analysis`
ADD COLUMN `overdue_10_total` INT(8) NULL DEFAULT 0 AFTER `overdue_10_days`,
ADD COLUMN `overdue_30_total` INT(8) NULL DEFAULT 0 AFTER `overdue_30_days`,
ADD COLUMN `overdue_60_total` INT(8) NULL DEFAULT 0 AFTER `overdue_60_days`,
ADD COLUMN `overdue_90_total` INT(8) NULL DEFAULT 0 AFTER `overdue_90_days`;


ALTER TABLE `ivuser`.`t_loan_repayment_detail`
ADD INDEX `index_userid_name` (`user_id` ASC, `username` ASC);


CREATE TABLE `t_bid_analysis_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `username` varchar(100) NOT NULL,
  `policy_id` bigint(20) NOT NULL,
  `policy_name` varchar(15) NOT NULL,
  `policy_type` tinyint(2) NOT NULL,
  `bid_count_total` int(5) DEFAULT '0',
  `bid_amount_total` int(8) DEFAULT '0',
  `bid_rate_avg` double DEFAULT '0',
  `bid_month_avg` double DEFAULT '0',
  `overdue_10_days` int(5) DEFAULT '0',
  `overdue_10_total` int(8) DEFAULT '0',
  `overdue_30_days` int(5) DEFAULT '0',
  `overdue_30_total` int(8) DEFAULT '0',
  `overdue_60_days` int(5) DEFAULT '0',
  `overdue_60_total` int(8) DEFAULT '0',
  `overdue_90_days` int(5) DEFAULT '0',
  `overdue_90_total` int(8) DEFAULT '0',
  `overdue_principal` double DEFAULT '0',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`user_id`,`username`,`policy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `ivuser`.`t_bid_analysis_policy`
ADD COLUMN `bid_age_avg` DOUBLE NULL DEFAULT 0 AFTER `bid_month_avg`;
ALTER TABLE `ivuser`.`t_bid_analysis_policy`
ADD COLUMN `bid_lender_count_avg` DOUBLE NULL DEFAULT 0 AFTER `bid_age_avg`;



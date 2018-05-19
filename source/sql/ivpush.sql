/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50623
 Source Host           : localhost
 Source Database       : ivpush

 Target Server Type    : MySQL
 Target Server Version : 50623
 File Encoding         : utf-8

 Date: 08/01/2017 21:36:53 PM
*/

CREATE SCHEMA `ivpush` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use ivpush;

-- ----------------------------
--  Table structure for `ivpush_sms_log`
-- ----------------------------
DROP TABLE IF EXISTS `ivpush_sms_log`;
CREATE TABLE `ivpush_sms_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tmp_group` varchar(45) NOT NULL,
  `tmp_code` varchar(45) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `data` varchar(45) DEFAULT NULL,
  `content` varchar(256) NOT NULL,
  `count_fail` smallint(2) NOT NULL DEFAULT '0',
  `count_ok` smallint(2) NOT NULL DEFAULT '0',
  `msg_return` varchar(45) DEFAULT NULL,
  `msg_id` varchar(45) DEFAULT NULL,
  `state` smallint(2) NOT NULL,
  `is_delete` bit(1) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ivpush_sms_log`
-- ----------------------------
BEGIN;
INSERT INTO `ivpush_sms_log` VALUES ('1', '0', '0', '18611410103', null, '【云片网】您的验证码是1234', '0', '1', null, '', '1', b'0', '2017-07-28 16:41:42', '2017-07-28 16:41:41');
COMMIT;

-- ----------------------------
--  Table structure for `ivpush_sms_template`
-- ----------------------------
DROP TABLE IF EXISTS `ivpush_sms_template`;
CREATE TABLE `ivpush_sms_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT '模板名称，唯一约束',
  `tmp_group` varchar(45) NOT NULL COMMENT '模板所属组，0-通用，1-会员，2-交易，3-产品',
  `tmp_code` varchar(45) NOT NULL COMMENT '模板所在group内所属业务code，0-通用，1-产品组ppd',
  `template` varchar(256) DEFAULT NULL COMMENT '模板内容',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '逻辑删除字段，1-删除，0-未删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `third_auth_ids` varchar(45) DEFAULT NULL COMMENT '第三方资源id列表，用逗号隔开，同一个模板可能需要多个id去发送，确保多个资源切换',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `group_code_unique` (`tmp_group`,`tmp_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ivpush_sms_template`
-- ----------------------------
BEGIN;
INSERT INTO `ivpush_sms_template` VALUES ('1', '验证码模板', '0', '0', '', null, b'0', '2017-07-28 10:11:15', '2017-07-28 10:11:18', '1');
COMMIT;

-- ----------------------------
--  Table structure for `ivpush_third_auth`
-- ----------------------------
DROP TABLE IF EXISTS `ivpush_third_auth`;
CREATE TABLE `ivpush_third_auth` (
  `id` bigint(20) NOT NULL,
  `name` varchar(128) NOT NULL,
  `third_key` varchar(128) NOT NULL,
  `third_secret` varchar(128) NOT NULL,
  `third_extend` varchar(128) DEFAULT NULL,
  `third_class` varchar(45) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `thirdAuthIds` varchar(45) NOT NULL,
  `type` smallint(2) NOT NULL DEFAULT '1' COMMENT '1-短信，2-push，3-mail',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ivpush_third_auth`
-- ----------------------------
BEGIN;
INSERT INTO `ivpush_third_auth` VALUES ('1', '云片', 'a18d7c646b28cbcaa189b59b8e084d0e', '1212sfsdf', null, 'yunpian', b'0', '2017-07-28 10:10:04', '2017-07-28 10:10:07', '', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

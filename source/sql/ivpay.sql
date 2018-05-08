CREATE SCHEMA `ivpay` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

CREATE TABLE `ivpay`.`t_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NOT NULL,
  `describtion` VARCHAR(128) NULL,
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `ivpay`.`t_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(100) NOT NULL,
  `product_id` BIGINT(20) NOT NULL,
  `order_type` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '订单类型：1-充值订单',
  `order_status` TINYINT(2) NOT NULL DEFAULT 0,
  `pay_status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '支付状态：1-未支付，2-已支付',
  `pay_time` DATETIME NOT NULL COMMENT '支付时间',
  `price` INT(8) NOT NULL DEFAULT 0 COMMENT '原价（分）',
  `pay_price` INT(8) NOT NULL DEFAULT 0 COMMENT '应付价格（分）',
  `alipay_price` INT(8) NOT NULL DEFAULT 0 COMMENT '支付宝支付金额',
  `couponpay_price` VARCHAR(45) NOT NULL DEFAULT 0 COMMENT '代金券支付金额',
  `buy_count` INT(8) NOT NULL DEFAULT 0 COMMENT '购买数量',
  `is_delete` BIT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `ivpay`.`t_order`
ADD COLUMN `user_id` BIGINT(20) NOT NULL AFTER `order_no`;


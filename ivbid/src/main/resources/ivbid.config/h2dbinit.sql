
CREATE TABLE `t_user_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` SMALLINT(2) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT 'ppd username',
  `policy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `month_begin` int(2) NOT NULL COMMENT '起始期限 月',
  `month_end` int(2) NOT NULL COMMENT '截止期限 月',
  `amount_begin` int(8) NOT NULL COMMENT '',
  `amount_end` int(8) NOT NULL COMMENT '',
  `rate_begin` DOUBLE NOT NULL COMMENT '',
  `rate_end` DOUBLE NOT NULL COMMENT '',
  `age_begin` smallint(3) NOT NULL,
  `age_end` smallint(3) NOT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `loaner_success_count_begin` smallint(5) NOT NULL COMMENT '借款人成功借款次数',
  `loaner_success_count_end` smallint(5) NOT NULL,
  `waste_count_begin` smallint(5) NOT NULL,
  `waste_count_end` smallint(5) NOT NULL,
  `normal_count_begin` smallint(5) NOT NULL,
  `normal_count_end` smallint(5) NOT NULL,
  `overdue_less_count_begin` smallint(5) NOT NULL,
  `overdue_less_count_end` smallint(5) NOT NULL,
  `overdue_more_count_begin` smallint(5) NOT NULL,
  `overdue_more_count_end` smallint(5) NOT NULL,
  `total_principal_begin` int(8) NOT NULL,
  `total_principal_end` int(8) NOT NULL,
  `owing_principal_begin` int(8) NOT NULL,
  `owing_principal_end` int(8) NOT NULL,
  `amount_to_receive_begin` int(8) NOT NULL,
  `amount_to_receive_end` int(8) NOT NULL,
  `amount_owing_total_begin` int(8) NOT NULL,
  `amount_owing_total_end` int(8) NOT NULL,
  `last_success_borrowdays_begin` smallint(3) NOT NULL,
  `last_success_borrowdays_end` smallint(3) NOT NULL,
  `register_borrow_months_begin` smallint(4) NOT NULL,
  `register_borrow_months_end` smallint(4) NOT NULL,
  `owing_highest_debt_ratio_begin` DOUBLE NOT NULL,
  `owing_highest_debt_ratio_end` DOUBLE NOT NULL,
  `amt_debt_rat_bg` DOUBLE NOT NULL,
  `amt_debt_rat_ed` DOUBLE NOT NULL,
  `creditcode_flag` bigint(20) NOT NULL COMMENT '打标值 用于匹配标的详情打标值',
  `thirdauthinfo_flag` bigint(20) NOT NULL COMMENT '',
  `certificate_flag` bigint(20) NOT NULL COMMENT '',
  `studystyle_flag` bigint(20) NOT NULL COMMENT '',
  `graduateschooltype_flag` bigint(20) NOT NULL COMMENT '',
  `bid_amount` int(8) NOT NULL COMMENT '单笔投资金额',
  PRIMARY KEY (`id`)
) CHARSET=utf8;
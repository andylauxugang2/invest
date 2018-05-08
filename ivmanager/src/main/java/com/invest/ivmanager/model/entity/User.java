package com.invest.ivmanager.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * 用户model
 * Created by xugang on 15/9/8.
 */
@Data
public class User extends BaseEntity {

	private static final long serialVersionUID = -5894602504757998179L;

	//columns START
	private String username; // 用户名
	private String password; // 密码
	private String name; // 姓名
	private String phone; // 电话
	private Integer status; // 状态
	//columns END

	private String menus; // JSON
	private String whereClause;
}

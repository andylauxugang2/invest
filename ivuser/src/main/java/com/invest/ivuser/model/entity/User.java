package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import com.invest.ivcommons.util.security.md5.MD5Util;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class User extends BaseEntity {
    private static final long serialVersionUID = 9208147191898045167L;
    public static final String NICK_DEFAULT = "拍客";

    private String mobile;
    private String password;
    private String referrerMobile;//推荐人手机号
    private String nick = NICK_DEFAULT;
    private String userToken;
    private String registerSource; //注册渠道来源 @see ClientTypeEnum
    private String securityKey; //密码加密的安全码 系统自动生成

    private String headImgOrg;
    private String headImg;

    private Date lastLoginTime;

    public String encryptPassword() {
        return MD5Util.md5(password + securityKey);
    }

    public void clearPwd() {
        this.password = null;
        this.securityKey = null;
    }
}

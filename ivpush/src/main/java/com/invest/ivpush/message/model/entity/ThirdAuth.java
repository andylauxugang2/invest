package com.invest.ivpush.message.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;

public class ThirdAuth extends BaseEntity {

    private static final long serialVersionUID = -3904085869325238040L;
    private Integer type;

    private String name;

    private String thirdKey;

    private String thirdSecret;

    private String thirdExtend;

    private String thirdClass;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getThirdKey() {
        return thirdKey;
    }

    public void setThirdKey(String thirdKey) {
        this.thirdKey = thirdKey == null ? null : thirdKey.trim();
    }

    public String getThirdSecret() {
        return thirdSecret;
    }

    public void setThirdSecret(String thirdSecret) {
        this.thirdSecret = thirdSecret == null ? null : thirdSecret.trim();
    }

    public String getThirdExtend() {
        return thirdExtend;
    }

    public void setThirdExtend(String thirdExtend) {
        this.thirdExtend = thirdExtend == null ? null : thirdExtend.trim();
    }

    public String getThirdClass() {
        return thirdClass;
    }

    public void setThirdClass(String thirdClass) {
        this.thirdClass = thirdClass == null ? null : thirdClass.trim();
    }

}
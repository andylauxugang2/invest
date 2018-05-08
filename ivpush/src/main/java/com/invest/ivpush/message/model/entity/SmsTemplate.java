package com.invest.ivpush.message.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class SmsTemplate extends BaseEntity {

    private static final long serialVersionUID = -4936540152717791851L;
    private String name;

    private String tmpGroup;

    private String tmpCode;

    private String template;

    private String remark;

    private String thirdAuthIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTmpGroup() {
        return tmpGroup;
    }

    public void setTmpGroup(String tmpGroup) {
        this.tmpGroup = tmpGroup == null ? null : tmpGroup.trim();
    }

    public String getTmpCode() {
        return tmpCode;
    }

    public void setTmpCode(String tmpCode) {
        this.tmpCode = tmpCode == null ? null : tmpCode.trim();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getThirdAuthIds() {
        return thirdAuthIds;
    }

    public void setThirdAuthIds(String thirdAuthIds) {
        this.thirdAuthIds = thirdAuthIds;
    }

    public List<Long> transferThirdAuthIds(String thirdAuthIds) {
        String[] temp = thirdAuthIds.split(",");
        List<Long> result = new ArrayList<>();
        for(String s : temp){
            result.add(Long.parseLong(s));
        }
        return result;
    }
}
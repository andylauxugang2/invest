package com.invest.ivmanager.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块model
 * Created by xugang on 15/9/8.
 */
@Data
public class Modle extends BaseEntity {

    private static final long serialVersionUID = -7065680679993502098L;

    //columns START
    private String modleName;
    private String modleAction;
    private String icon;
    private Long parentId;
    private Integer status;
    private Integer sort;
    //columns END

    private List<Modle> leaves = new ArrayList<>(); // 叶子节点
}

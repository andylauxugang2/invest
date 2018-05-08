package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class NotifyMessage extends BaseEntity {

    private static final long serialVersionUID = -1384772917577629139L;

    private String title;
    private String content;
    private Short type; // @see NotifyMessageTypeEnum
    private Short status;
    private String link;
    private String uniqueKey;

    //vo
    private String createTimeFormat;
}

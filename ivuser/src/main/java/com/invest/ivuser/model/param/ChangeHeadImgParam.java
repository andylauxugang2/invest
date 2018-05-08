package com.invest.ivuser.model.param;

import lombok.Data;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class ChangeHeadImgParam extends UserBaseParam {

    private String fileName;
    private byte[] content; //原图
}

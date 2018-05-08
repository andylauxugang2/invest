package com.invest.ivusergateway.model.request;

import lombok.Data;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.InputStream;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class ChangeHeadImgReq {

    private Long userId;
    private InputStream userFace;
}

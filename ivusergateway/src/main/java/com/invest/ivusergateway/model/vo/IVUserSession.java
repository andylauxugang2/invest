package com.invest.ivusergateway.model.vo;

import com.invest.ivuser.model.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by xugang on 2017/8/14.
 */
@Data
public class IVUserSession implements Serializable {
    private static final long serialVersionUID = 8511731813262669055L;

    private User user;
}

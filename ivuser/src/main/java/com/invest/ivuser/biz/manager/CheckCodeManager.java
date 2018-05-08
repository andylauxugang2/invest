package com.invest.ivuser.biz.manager;

import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.CheckCode;
import org.springframework.stereotype.Component;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class CheckCodeManager extends BaseManager {

    public void saveCheckCode(CheckCode checkCode) {
        try {
            checkCodeDAO.insert(checkCode);
        } catch (Exception e) {
            logger.error("保存验证码失败,mobile=" + checkCode.getMobile() + ",code=" + checkCode.getCheckCode(), e);
            throw new IVDAOException(e);
        }
        logger.info("保存验证码成功,mobile={},code={}", checkCode.getMobile(), checkCode.getCheckCode());
    }


    public CheckCode getCheckCode(CheckCode checkCode) {
        CheckCode result;
        try {
            result = checkCodeDAO.selectOneBySelective(checkCode);
        } catch (Exception e) {
            logger.error("查询验证码失败,mobile=" + checkCode.getMobile() + ",type=" + checkCode.getType(), e);
            throw new IVDAOException(e);
        }
        return result;
    }
}

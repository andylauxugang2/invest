package com.invest.ivuser.biz.manager;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.BlackListThird;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class BlackListThirdManager extends BaseManager {
    public void saveBlackListThird(BlackListThird blackListThird) {
        try {
            int line = blackListThirdDAO.insert(blackListThird);
            if (line == 1) {
                logger.info("插入blackListThird成功,blackListThird={}", JSONObject.toJSONString(blackListThird));
            } else {
                throw new IVDAOException("插入blackListThird数据库返回行数不为1");
            }
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public List<BlackListThird> getBlackListThirdList(BlackListThird blackListThird) {
        try {
            return blackListThirdDAO.selectListBySelective(blackListThird);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public boolean deleteBlackListThird(String type, String value) {
        try {
            int line = blackListThirdDAO.deleteByUniqueKey(type, value);
            if (line == 1) {
                return true;
            }
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return false;
    }
}

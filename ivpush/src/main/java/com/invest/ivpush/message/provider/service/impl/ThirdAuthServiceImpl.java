package com.invest.ivpush.message.provider.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.invest.ivpush.message.model.dataobject.Pagination;
import com.invest.ivpush.message.model.dataobject.ThirdAuthSo;
import com.invest.ivpush.message.model.dataobject.ThirdAuthType;
import com.invest.ivpush.message.model.entity.ThirdAuth;
import com.invest.ivpush.message.provider.dao.ThirdAuthMapper;
import com.invest.ivpush.message.provider.exception.ServiceException;
import com.invest.ivpush.message.service.SmsTemplateService;
import com.invest.ivpush.message.service.ThirdAuthService;
import com.invest.ivpush.message.service.exception.PushException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("thirdAuthService")
public class ThirdAuthServiceImpl implements ThirdAuthService {

    @Resource
    private ThirdAuthMapper thirdAuthMapper;

    @Resource
    private SmsTemplateService smsTemplateService;

    public ThirdAuth get(Long id) {
        return thirdAuthMapper.selectByPrimaryKey(id);
    }

    /**
     * 新添加
     *
     * @param log
     * @return
     */
    public Long save(ThirdAuth log) throws PushException {
        log.setCreateTime(new Date());
        log.setIsDelete(false);
        thirdAuthMapper.insert(log);
        return log.getId();
    }

    /**
     * 修改
     *
     * @param log
     * @return
     */
    public Long update(ThirdAuth log) throws PushException {
        log.setUpdateTime(new Date());
        thirdAuthMapper.updateByPrimaryKey(log);
        return log.getId();
    }

    /**
     * 修改
     *
     * @param log
     * @return
     */
    public Long update(Long id, ThirdAuth log) throws PushException {
        ThirdAuth po = this.get(id);
        String[] ignoreProperties = {"id", "createTime"};
        BeanUtils.copyProperties(log, po, ignoreProperties);
        this.update(po);
        return id;
    }

    /**
     * 删除短信模板
     *
     * @param id
     * @throws PushException
     */
    public Long delete(Long id) throws PushException {
        ThirdAuth auth = this.get(id);
        if (auth.getType() == ThirdAuthType.TYPE_SMS) {
            if (smsTemplateService.isExistByThirdAuthId(id)) {
                throw new ServiceException("有短信模板在使用“" + auth.getName() + "”的第三方帐号信息发送短信！");
            }
        }
        auth.setIsDelete(true);
        return update(auth);
    }

    /**
     * 获取所有短信模板
     *
     * @throws PushException
     */
    public List<ThirdAuth> findAll() throws PushException {
        return thirdAuthMapper.selectByAll();
    }

    @Override
    public Pagination findByPage(Pagination page, ThirdAuthSo so) {
        List<ThirdAuth> list = thirdAuthMapper.selectByPage(page, so);
        page.setList(list);
        return page;
    }

    @Override
    public List<ThirdAuth> findListByType(Integer type) {
        return thirdAuthMapper.selectByType(type);
    }

}

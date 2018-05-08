package com.invest.ivpush.message.provider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.invest.ivpush.message.model.entity.ThirdAuth;
import com.invest.ivpush.message.provider.plus.MessagePlus;
import com.invest.ivpush.message.provider.plus.SmsMessagePlus;
import com.invest.ivpush.message.provider.plus.sms.SmsPlusYunpian;
import com.invest.ivpush.message.provider.service.MessagePlusService;
import com.invest.ivpush.message.service.ThirdAuthService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xusm
 * 
 */
@Service("messagePlusService")
public class MessagePlusServiceImpl implements MessagePlusService {

	private static final Log logger = LogFactory.getLog(MessagePlusServiceImpl.class);

	private static Map<String, MessagePlus> messagePlusMap = new HashMap<>();

	@Autowired
	private SmsPlusYunpian smsPlusYunpian;

	@PostConstruct
	private void init(){
		messagePlusMap.put(SmsMessagePlus.SMSMESSAGEPLUS_THIRD_TYPE_YUNPIAN, smsPlusYunpian);
	}

	@Resource
	private ThirdAuthService thirdAuthService;

	@Override
	public MessagePlus genrate(Long id) {
		MessagePlus plus = null;
		try {
			ThirdAuth thirdAuth = thirdAuthService.get(id);
			if (null != thirdAuth) {
				Class<?> clazz = Class.forName(thirdAuth.getThirdClass());
				plus = (MessagePlus) clazz.newInstance();
				plus.setThirdKey(thirdAuth.getThirdKey());
				plus.setThirdSecret(thirdAuth.getThirdSecret());
				plus.setThirdExtend(thirdAuth.getThirdExtend());
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return plus;
	}
	
	@Override
	public List<MessagePlus> genrate(List<Long> ids) {
		List<MessagePlus> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(ids)){
			logger.warn("参数第三方资源ids为空");
			return list;
		}
		ids.stream().forEach(id -> {
			//获取第三方资源
			ThirdAuth thirdAuth = thirdAuthService.get(id);
			//根据ThirdClass常量寻找短信插件
			MessagePlus plus = messagePlusMap.get(thirdAuth.getThirdClass());
			plus.setThirdKey(thirdAuth.getThirdKey());
			plus.setThirdSecret(thirdAuth.getThirdSecret());
			plus.setThirdExtend(thirdAuth.getThirdExtend());
			list.add(plus);
		});
		return list;

	}

}

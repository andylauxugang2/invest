package com.invest.ivppad;

import com.invest.ivcommons.rocketmq.model.LoanableDetailMessage;
import com.invest.ivcommons.util.endecode.Base64Util;
import com.invest.ivcommons.util.serialize.HessianUtil;
import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import com.invest.ivppad.model.param.PPDOpenApiGetLoanListingDetailBatchParam;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/11/8.do best.
 */
public class TestMQProducerTest extends TestBase {

    @Resource
    private LoanService loanService;

    @Resource
    private TestMQProducer testMQProducer;

    @Test
    public void testSendMsg() throws Exception {
        List<Integer> listIds = new ArrayList<>();
        listIds.add(82009332);
        //单次下载标的详情&通知策略任务
        PPDOpenApiGetLoanListingDetailBatchParam param = new PPDOpenApiGetLoanListingDetailBatchParam();
        param.setListingIds(listIds);
        //批量下载标的详情信息 一次接口返回10条数据 单次下载耗时为5000毫秒左右,多线程下载平均单次查询耗时为350毫秒
        PPDOpenApiLoanResult result = loanService.getLoanListingDetailBatch(param);
        if (result.isFailed()) {
            logger.error("获取散标详情信息失败,error={}", result);
            throw new RuntimeException(result.getErrorMsg());
        }
        List<PPDOpenApiLoanListingDetailBatchResponse.LoanListingDetail> loanListingDetails = result.getLoanListingDetailList();

        String message = Base64Util.encodeToString(HessianUtil.toBytes(loanListingDetails));
        LoanableDetailMessage msg = new LoanableDetailMessage(1, message);
        testMQProducer.sendMsg(msg);
    }
}
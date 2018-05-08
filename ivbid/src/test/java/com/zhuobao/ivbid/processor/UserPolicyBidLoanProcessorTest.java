package com.zhuobao.ivbid.processor;

import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivppad.model.http.response.PPDOpenApiLoanListingDetailBatchResponse;
import com.invest.ivppad.model.param.PPDOpenApiGetLoanListingDetailBatchParam;
import com.invest.ivppad.model.result.PPDOpenApiLoanResult;
import com.zhuobao.ivbid.TestBase;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/11/20.do best.
 */
public class UserPolicyBidLoanProcessorTest extends TestBase {

    @Resource
    private UserPolicyBidLoanProcessor userPolicyBidLoanProcessor;

    @Resource
    private LoanService loanService;

    @Test
    public void testProcess() throws Exception {
        List<Integer> listIds = new ArrayList<>();
        listIds.add(80913399);
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

        userPolicyBidLoanProcessor.process(loanListingDetails, 1);
    }
}
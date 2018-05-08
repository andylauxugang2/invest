package com.zhuobao.ivsyncjob.job.elastic;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.invest.ivuser.dao.db.LoanRepaymentDetailDAO;
import com.invest.ivuser.dao.db.UserThirdBindInfoDAO;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.zhuobao.ivsyncjob.common.error.ElasticExecuteError;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2017/9/18.
 */
@Component
public class CleanDataJob extends ElasticBaseJob {

    @Autowired
    protected LoanRepaymentDetailDAO loanRepaymentDetailDAO;
    @Autowired
    protected UserThirdBindInfoDAO userThirdBindInfoDAO;

    @Override
    public void doExecute(ShardingContext shardingContext) throws ElasticExecuteError {
        int shardingCount = shardingContext.getShardingTotalCount();
        int shardingItem = shardingContext.getShardingItem();

        UserThirdBindInfo param = new UserThirdBindInfo();
        List<String> thirdUUIDList = userThirdBindInfoDAO.selectAllThirdUUIDList(param);
        if (CollectionUtils.isEmpty(thirdUUIDList)) {
            logger.error("获取第三方账号列表为空,不去同步用户账户信息");
            return;
        }

        int batchSize = 100;
        thirdUUIDList.parallelStream().forEach(thirdUUID -> {
            List<LoanRepaymentDetail> loanRepaymentDetailList = loanRepaymentDetailDAO.selectRepeat(thirdUUID);
            if (CollectionUtils.isEmpty(loanRepaymentDetailList)) {
                return;
            }

            loanRepaymentDetailList.stream().forEach(detail -> {
                List<LoanRepaymentDetail> loanRepaymentDetails = loanRepaymentDetailDAO.selectRepeatDetail(detail.getUserId(), detail.getUsername(), detail.getListingId(), detail.getOrderId());
                if (CollectionUtils.isEmpty(loanRepaymentDetails)) {
                    return;
                }

                List<Long> ids = new ArrayList<>(batchSize);
                for (int i = 0; i < loanRepaymentDetails.size() - 1; i++) {
                    ids.add(loanRepaymentDetails.get(i).getId());
                }


                int counter = 0;
                List<Long> idsTemp = new ArrayList<>(batchSize);
                for (Long id : ids) {
                    idsTemp.add(id);
                    counter++;
                    //存够batchSize条发给下游线程处理
                    if (counter % batchSize == 0) {
                        //删除
                        idsTemp = new ArrayList<>(batchSize);
                        loanRepaymentDetailDAO.deleteRepeat(idsTemp);
                    }

                }
                if (idsTemp.size() > 0) {
                    //删除
                    loanRepaymentDetailDAO.deleteRepeat(idsTemp);
                }

            });
        });


    }
}
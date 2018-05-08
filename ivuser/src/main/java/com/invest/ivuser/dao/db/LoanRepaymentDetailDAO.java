package com.invest.ivuser.dao.db;

import com.invest.ivcommons.dal.base.BaseDAO;
import com.invest.ivuser.dao.query.LoanRepaymentDetailQuery;
import com.invest.ivuser.model.entity.LoanRepaymentDetail;
import com.invest.ivuser.model.entity.ext.LoanRepaymentOverdueDetail;
import com.invest.ivuser.model.entity.ext.OverdueTotalCount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface LoanRepaymentDetailDAO extends BaseDAO<LoanRepaymentDetail> {

    int batchInsert(List<LoanRepaymentDetail> records);

    List<LoanRepaymentDetail> selectListByQuery(LoanRepaymentDetailQuery loanRepaymentDetailQuery);

    /**
     * 统计相关sql
     */
    List<OverdueTotalCount> selectOverdueTotalCount(LoanRepaymentDetailQuery loanRepaymentDetailQuery);

    List<LoanRepaymentDetail> selectOverdueDetail(LoanRepaymentDetailQuery loanRepaymentDetailQuery);






    /**
     * 查询重复的还款计划数据
     * @return
     */
    List<LoanRepaymentDetail> selectRepeat(String username);
    List<LoanRepaymentDetail> selectRepeatDetail(@Param(value = "userId") Long userId,
                                                 @Param(value = "username") String username,
                                                 @Param(value = "listingId") Integer listingId,
                                                 @Param(value = "orderId") Integer orderId);
    void deleteRepeat(@Param("ids") List<Long> ids);
}
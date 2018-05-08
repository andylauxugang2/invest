package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.BidAnalysis;
import com.invest.ivuser.model.vo.LoanRepaymentDetailVO;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class DataResult extends Result {
    private static final long serialVersionUID = -6209450697963255965L;

    private BidAnalysis bidAnalysis;
    private List<BidAnalysis> bidAnalysisList;

    private List<LoanRepaymentDetailVO> loanRepaymentDetailVOList;
}

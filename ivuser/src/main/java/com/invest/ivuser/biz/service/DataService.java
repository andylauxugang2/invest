package com.invest.ivuser.biz.service;

import com.invest.ivuser.model.entity.BidAnalysis;
import com.invest.ivuser.model.result.DataResult;

/**
 * Created by xugang on 2017/7/28.
 */
public interface DataService {

    DataResult getOverdueAnalysisById(Long id);

    DataResult getOverdueAnalysisList(BidAnalysis bidAnalysis);

    DataResult getOverdueAnalysisDetailList(Long analysisId, Short overdueType);
}

package com.invest.ivuser.biz.manager;

import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivuser.model.entity.BidAnalysis;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Component
public class BidAnalysisManager extends BaseManager {


    public List<BidAnalysis> getBidAnalysisList(BidAnalysis BidAnalysis) {
        List<BidAnalysis> result;
        try {
            result = bidAnalysisDAO.selectListBySelective(BidAnalysis);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    public void saveBidAnalysis(BidAnalysis BidAnalysis) {
        try {
            int line = bidAnalysisDAO.insert(BidAnalysis);
            if (line != 1) {
                throw new IVDAOException("插入BidAnalysis数据库返回行数不为1");
            }
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
    }

    public BidAnalysis getBidAnalysisById(Long id) {
        BidAnalysis result;
        try {
            result = bidAnalysisDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }
}

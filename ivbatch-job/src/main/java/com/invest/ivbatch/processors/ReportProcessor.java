package com.invest.ivbatch.processors;

import com.invest.ivbatch.model.Report;
import org.springframework.batch.item.ItemProcessor;

/**
 * 如果ReportProcessor return null batch_step_execution表-字段FILTER_COUNT会+1统计
 * Created by xugang on 2016/9/20.
 */
public class ReportProcessor implements ItemProcessor<Report, Report> {
    @Override
    public Report process(Report item) throws Exception {
        System.out.println("process=" + item.toString());
        if (item.getId() == 10) {
            throw new IllegalArgumentException("10不可读");
            //throw new IllegalStateException("10不可读");
        }
        return item;
    }
}

package com.invest.ivbatch.writers;

import com.invest.ivbatch.model.Report;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * 如果ReportProcessor return null,则会出现0的情况，writer参数传入List为空
 * batch_step_execution表-WRITE_COUNT字段标识
 */
public class ReportWriter implements ItemWriter<Report> {

    @Override
    public void write(List<? extends Report> items) throws Exception {
        System.out.println("writer..." + items.size());
        for (Report item : items) {
            System.out.println("writer..." + item);
        }
    }

}
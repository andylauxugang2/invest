package com.invest.ivuser.model.result;

import com.invest.ivcommons.base.result.Result;
import com.invest.ivuser.model.entity.UserLoanRecord;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class UserLoanResult extends Result {
    private static final long serialVersionUID = -6209450697963255965L;

    private List<UserLoanRecord> userLoanRecordList;
    private int count;
}

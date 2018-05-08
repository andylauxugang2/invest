package com.invest.ivmanager.model.vo;

import com.invest.ivuser.model.entity.LoanPolicy;
import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/8/16.
 */
@Data
public class LoanPolicyVO extends LoanPolicy {

    private List<Boolean> graduateSchoolTypeFlagList;
}

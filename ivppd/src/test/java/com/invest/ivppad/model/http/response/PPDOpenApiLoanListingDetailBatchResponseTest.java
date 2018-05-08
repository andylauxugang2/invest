package com.invest.ivppad.model.http.response;

import com.invest.ivppad.common.*;
import com.invest.ivppad.common.SchoolTypeEnum;
import junit.framework.TestCase;

/**
 * Created by xugang on 2017/8/29.do best.
 */
public class PPDOpenApiLoanListingDetailBatchResponseTest extends TestCase {

    public void testHitFlag() throws Exception {
        long flag = 6442450944L;
        for (PPDCreditCodeEnum _enum : PPDCreditCodeEnum.values()) {
            if (PPDBinExpConstants.testFlagMatch(flag, _enum.getCode())) {
                System.out.println(_enum.getDesc());
            }
        }
        for (PPDStudyStyleEnum _enum : PPDStudyStyleEnum.values()) {
            if (PPDBinExpConstants.testFlagMatch(flag, _enum.getCode())) {
                System.out.println(_enum.getDesc());
            }
        }
        for (PPDCertificateEnum _enum : PPDCertificateEnum.values()) {
            if (PPDBinExpConstants.testFlagMatch(flag, _enum.getCode())) {
                System.out.println(_enum.getDesc());
            }
        }
        for (PPDThridAuthValidEnum _enum : PPDThridAuthValidEnum.values()) {
            if (PPDBinExpConstants.testFlagMatch(flag, _enum.getCode())) {
                System.out.println(_enum.getDesc());
            }
        }
        for (SchoolTypeEnum _enum : SchoolTypeEnum.values()) {
            if (PPDBinExpConstants.testFlagMatch(flag, _enum.getCode())) {
                System.out.println(_enum.getName());
            }
        }

        System.out.println((1024 & -1) == -1);//BITAND(total_flag,#{totalFlag}) = #{totalFlag}

    }
}
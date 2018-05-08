package com.invest.ivmanager.model.request;

import lombok.Data;

import java.util.List;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class SaveLoanPolicyReq {

    private Long id;
    private Long userId; //为空表示 后台修改系统散标策略，所有使用该策略的用户都要实时生效 而自定义为用户id
    private Short policyType;
    private String userPolicyName;
    private Integer monthBegin;
    private Integer monthEnd;
    private Double rateBegin;
    private Double rateEnd;
    private Integer amountBegin;
    private Integer amountEnd;
    private Integer ageBegin;
    private Integer ageEnd;
    private Short sex; //1-男 0-女
    private List<Long> creditCode;
    private List<Long> certificate;
    private List<Long> studyStyle;
    private List<Long> graduateSchoolType;
    private List<Long> thirdAuthInfo;
    private Integer loanerSuccessCountBegin;
    private Integer loanerSuccessCountEnd;
    private Integer wasteCountBegin;
    private Integer wasteCountEnd;
    private Integer normalCountBegin;
    private Integer normalCountEnd;
    private Integer overdueLessCountBegin;
    private Integer overdueLessCountEnd;
    private Integer overdueMoreCountBegin;
    private Integer overdueMoreCountEnd;
    private Integer totalPrincipalBegin;
    private Integer totalPrincipalEnd;
    private Integer owingPrincipalBegin;
    private Integer owingPrincipalEnd;
    private Integer amountToReceiveBegin;
    private Integer amountToReceiveEnd;
    private Integer amountOwingTotalBegin;
    private Integer amountOwingTotalEnd;
    private Integer lastSuccessBorrowDaysBegin;
    private Integer lastSuccessBorrowDaysEnd;
    private Integer registerBorrowMonthsBegin; //本次借款距注册时间月数
    private Integer registerBorrowMonthsEnd;
    private Double owingHighestDebtRatioBegin;
    private Double owingHighestDebtRatioEnd;
    private Double amtDebtRatBg;
    private Double amtDebtRatEd;

    //默认-1 长期
    private Integer validTime = -1;


}

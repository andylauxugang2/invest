package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import com.invest.ivcommons.util.date.DateUtil;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by xugang on 2017/7/28.
 */
@Data
public class LoanPolicy extends BaseEntity {
    private static final long serialVersionUID = 7452287252716909568L;

    public static final short STATUS_ON = 1;
    public static final short STATUS_OFF = 0;

    public static final int VALIDTIME = 10;

    public static final int NUMBER_NULL_VALUE = 0;

    private Long userId;
    private String name; //策略名称 可重复
    private Short policyType;
    private Short riskLevel;
    private Date validTime = DateUtil.dateAddFromToday(VALIDTIME, Calendar.YEAR); //有效期
    private Short status = STATUS_ON; //策略状态 开启,停止,废弃
    private Integer monthBegin;
    private Integer monthEnd;
    private Double rateBegin;
    private Double rateEnd;
    private Integer amountBegin; //借款起始金额
    private Integer amountEnd; //借款截止金额
    private long creditCode; //魔镜等级打标值
    private long thirdAuthInfo; //第三方认证打标值
    private long certificate; //学历认证打标值
    private long studyStyle; //学习形式打标值
    private long graduateSchoolType; //毕业学校分类打标值
    private Integer loanerSuccessCountBegin; //成功借款次数
    private Integer loanerSuccessCountEnd; //成功借款次数

    private Integer ageBegin;
    private Integer ageEnd;
    private Short sex;

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

    //待还金额/历史最高负债
    private Double owingHighestDebtRatioBegin;
    private Double owingHighestDebtRatioEnd;

    //本次借款/历史最高负债 amountHighestDebtRatioBegin
    private Double amtDebtRatBg;
    private Double amtDebtRatEd;

    private Long totalFlag; //总打标值

    public Long calculateSumFlag() {
        long nv = creditCode | thirdAuthInfo | certificate | studyStyle | graduateSchoolType;
        //System.err.println(nv + "=" + Long.toBinaryString(nv));
        return nv;
    }
}

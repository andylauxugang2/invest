package com.invest.ivuser.model.entity;

import com.invest.ivcommons.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xugang on 2017/8/3.
 */
@Data
public class LoanDetail extends BaseEntity {

    private static final long serialVersionUID = -8386196249791335447L;

    private int listingId; //可投标列表页id 列表编号
    private Date fistBidTime; //首次投资时间
    private Date lastBidTime; //末笔投资时间
    private int lenderCount; //投标人数
    //private String auditingTime; //成交日期
    private String creditCode; //模型等级 标的等级
    private int amount; //借款金额
    private int months; //期限
    private double rate; //利率
    private String borrowName; //借款人的用户名
    private int gender; //性别	1 男 2 女 0 未知
    private int age; //借款人年龄
    private String educationDegree; //学历 专科\本科
    private String graduateSchool; //毕业学校
    private String studyStyle; //学习形式
    private int successCount; //成功借款次数
    private int wasteCount; //流标次数
    private int cancelCount; //撤标次数
    private int failedCount; //失败次数 没人投标
    private int normalCount; //正常还清次数
    private int overdueLessCount; //逾期(1-15)还清次数
    private int overdueMoreCount; //逾期(15天以上)还清次数
    private int owingPrincipal; //剩余待还本金
    private int owingAmount; //待还金额
    private int amountToReceive; //待收金额
    private Date firstSuccessBorrowTime; //第一次成功借款时间
    private Date lastSuccessBorrowTime; //最后一次成功借款时间
    private Date registerTime; //注册时间
    private double highestPrincipal; //单笔最高借款金额
    private double highestDebt; //历史最高负债
    private double totalPrincipal; //累计借款金额

    private Long thirdAuthFlag;
}

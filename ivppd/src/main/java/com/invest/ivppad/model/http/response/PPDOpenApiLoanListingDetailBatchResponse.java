package com.invest.ivppad.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.ivppad.common.*;
import com.invest.ivppad.util.BaseDataUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * {
 * "LoanInfos": [
 * {
 * "FistBidTime": null,
 * "LastBidTime": null,
 * "LenderCount": 0,
 * "AuditingTime": null,
 * "RemainFunding": 1500,
 * "DeadLineTimeOrRemindTimeStr": "2016/11/19",
 * "CreditCode": "AA",
 * "ListingId": 23886149,
 * "Amount": 1500,
 * "Months": 42,
 * "CurrentRate": 12.0085,
 * "BorrowName": "zhangsan",
 * "Gender": 1,
 * "EducationDegree": "专科",
 * "GraduateSchool": "四川工业科技学院",
 * "StudyStyle": "普通",
 * "Age": 22,
 * "SuccessCount": 0,
 * "WasteCount": 0,
 * "CancelCount": 0,
 * "FailedCount": 0,
 * "NormalCount": 0,
 * "OverdueLessCount": 0,
 * "OverdueMoreCount": 0,
 * "OwingPrincipal": 0,
 * "OwingAmount": 0,
 * "AmountToReceive": 0,
 * "FirstSuccessBorrowTime": null,
 * "LastSuccessBorrowTime": null,
 * "RegisterTime": "2016-11-04T04:57:40.473",
 * "CertificateValidate": 1,
 * "NciicIdentityCheck": 0,
 * "PhoneValidate": 1,
 * "VideoValidate": 0,
 * "CreditValidate": 0,
 * "EducateValidate": 1,
 * "HighestPrincipal":500.00,
 * "HighestDebt":500.00,
 * "TotalPrincipal":500.00
 * },
 * {
 * "xx": "……"
 * }
 * ],
 * "Result": 1,
 * "ResultMessage": "",
 * "ResultCode": null
 * }
 * Created by xugang on 17/01/16.
 */
@Data
public class PPDOpenApiLoanListingDetailBatchResponse extends PPDOpenApiLoanBaseResponse {

    @JsonProperty("LoanInfos")
    private List<LoanListingDetail> loanListingDetailList;

    /**
     * controller 接口返回样例
     * {
     * "creditCodeFlag": 1024,
     * "studyStyleFlag": 0,
     * "thirdAuthFlag": 0,
     * "educationFlag": 0,
     * "graduateSchoolFlag": 0,
     * "FistBidTime": "2017-09-01T14:55:30.543",
     * "LastBidTime": "2017-09-01T15:28:11.8",
     * "LenderCount": 11,
     * "AuditingTime": "2000-01-01T00:00:00",
     * "RemainFunding": 150,
     * "DeadLineTimeOrRemindTimeStr": "19天22时59分",
     * "CreditCode": "D",
     * "ListingId": 68884447,
     * "Amount": 1000,
     * "Months": 6,
     * "CurrentRate": 22,
     * "BorrowName": "lau2june",
     * "Gender": 2,
     * "Age": 22,
     * "EducationDegree": null,
     * "GraduateSchool": null,
     * "StudyStyle": null,
     * "SuccessCount": 3,
     * "WasteCount": 1,
     * "CancelCount": 0,
     * "FailedCount": 0,
     * "NormalCount": 7,
     * "OverdueLessCount": 0,
     * "OverdueMoreCount": 0,
     * "OwingPrincipal": 5959,
     * "OwingAmount": 6845,
     * "AmountToReceive": 0,
     * "FirstSuccessBorrowTime": "2017-05-29T18:20:06",
     * "LastSuccessBorrowTime": "2017-08-09T22:16:49",
     * "RegisterTime": "2014-03-31T03:04:42",
     * "CertificateValidate": false,
     * "NciicIdentityCheck": false,
     * "PhoneValidate": false,
     * "VideoValidate": false,
     * "CreditValidate": false,
     * "EducateValidate": false,
     * "HighestPrincipal": 3000,
     * "HighestDebt": 8171.12,
     * "TotalPrincipal": 7475
     * }
     */
    @Data
    public static class LoanListingDetail implements Serializable {
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKOWN = 0;

        private static final long serialVersionUID = -5981970425387277101L;
        @JsonProperty("FistBidTime")
        private String fistBidTime; //首次投资时间
        @JsonProperty("LastBidTime")
        private String lastBidTime; //末笔投资时间
        @JsonProperty("LenderCount")
        private int lenderCount; //投标人数
        @JsonProperty("AuditingTime")
        private String auditingTime; //成交日期
        @JsonProperty("RemainFunding")
        private int remainFunding; //剩余可投金额
        @JsonProperty("DeadLineTimeOrRemindTimeStr")
        private String deadLineTimeOrRemindTimeStr; //截止时间 2016/11/19或者14天15时57分(剩余时间)
        @JsonProperty("CreditCode")
        private String creditCode; //模型等级 标的等级
        @JsonProperty("ListingId")
        private int listingId; //可投标列表页id 列表编号
        @JsonProperty("Amount")
        private int amount; //借款金额
        @JsonProperty("Months")
        private int months; //期限
        @JsonProperty("CurrentRate")
        private double currentRate; //利率
        @JsonProperty("BorrowName")
        private String borrowName; //借款人的用户名
        @JsonProperty("Gender")
        private int gender; //性别	1 男 2 女 0 未知
        @JsonProperty("Age")
        private int age; //借款人年龄
        @JsonProperty("EducationDegree")
        private String educationDegree; //学历 专科\本科
        @JsonProperty("GraduateSchool")
        private String graduateSchool; //毕业学校
        @JsonProperty("StudyStyle")
        private String studyStyle; //学习形式
        @JsonProperty("SuccessCount")
        private int successCount; //成功借款次数
        @JsonProperty("WasteCount")
        private int wasteCount; //流标次数
        @JsonProperty("CancelCount")
        private int cancelCount; //撤标次数
        @JsonProperty("FailedCount")
        private int failedCount; //失败次数 没人投标
        @JsonProperty("NormalCount")
        private int normalCount; //正常还清次数
        @JsonProperty("OverdueLessCount")
        private int overdueLessCount; //逾期(1-15)还清次数
        @JsonProperty("OverdueMoreCount")
        private int overdueMoreCount; //逾期(15天以上)还清次数
        @JsonProperty("OwingPrincipal")
        private int owingPrincipal; //剩余待还本金
        @JsonProperty("OwingAmount")
        private int owingAmount; //待还金额
        @JsonProperty("AmountToReceive")
        private int amountToReceive; //待收金额
        @JsonProperty("FirstSuccessBorrowTime")
        private String firstSuccessBorrowTime; //第一次成功借款时间
        @JsonProperty("LastSuccessBorrowTime")
        private String lastSuccessBorrowTime; //最后一次成功借款时间
        @JsonProperty("RegisterTime")
        private String registerTime; //注册时间
        @JsonProperty("CertificateValidate")
        private boolean certificateValidate; //学历认证	0 未认证 1已认证
        @JsonProperty("NciicIdentityCheck")
        private boolean nciicIdentityCheck; //户籍认证	0 未认证 1已认证
        @JsonProperty("PhoneValidate")
        private boolean phoneValidate; //手机认证
        @JsonProperty("VideoValidate")
        private boolean videoValidate; //视频认证
        @JsonProperty("CreditValidate")
        private boolean creditValidate; //征信认证	0 未认证 1已认证
        @JsonProperty("EducateValidate")
        private boolean educateValidate; //学籍认证	0 未认证 1已认证
        @JsonProperty("HighestPrincipal")
        private double highestPrincipal; //单笔最高借款金额
        @JsonProperty("HighestDebt")
        private double highestDebt; //历史最高负债
        @JsonProperty("TotalPrincipal")
        private double totalPrincipal; //累计借款金额

        //自定义 转换标值
        private long creditCodeFlag;

        public void setCreditCode(String creditCode) {
            this.creditCode = creditCode;
            if (creditCode != null) {
                this.creditCodeFlag = PPDCreditCodeEnum.findByDesc(creditCode).getCode();
            }
        }

        private long studyStyleFlag;

        public void setStudyStyle(String studyStyle) {
            this.studyStyle = studyStyle;
            if (studyStyle != null) {
                this.studyStyleFlag = PPDStudyStyleEnum.findByDesc(studyStyle).getCode();
            }
        }

        private long thirdAuthFlag;

        public void setCertificateValidate(boolean certificateValidate) {
            this.certificateValidate = certificateValidate;
            if (certificateValidate) { //学历认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.certificateValid.getCode());
            }
        }

        public void setNciicIdentityCheck(boolean nciicIdentityCheck) {
            this.nciicIdentityCheck = nciicIdentityCheck;
            if (nciicIdentityCheck) { //户籍认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.nciicIdentityValid.getCode());
            }
        }

        public void setEducateValidate(boolean educateValidate) {
            this.educateValidate = educateValidate;
            if (educateValidate) { //学籍认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.educateValid.getCode());
            }
        }

        public void setPhoneValidate(boolean phoneValidate) {
            this.phoneValidate = phoneValidate;
            if (phoneValidate) { //手机认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.phoneValid.getCode());
            }
        }

        public void setVideoValidate(boolean videoValidate) {
            this.videoValidate = videoValidate;
            if (videoValidate) { //视频认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.videoValid.getCode());
            }
        }

        public void setCreditValidate(boolean creditValidate) {
            this.creditValidate = creditValidate;
            if (creditValidate) { //征信认证打标
                thirdAuthFlag = PPDBinExpConstants.onBit(thirdAuthFlag, PPDThridAuthValidEnum.creditValid.getCode());
            }
        }

        private long educationFlag;

        public void setEducationDegree(String educationDegree) {
            this.educationDegree = educationDegree;
            if (educationDegree != null) {
                this.educationFlag = PPDCertificateEnum.findByDesc(educationDegree).getCode();
            }
        }

        private long graduateSchoolFlag;

        public void setGraduateSchool(String graduateSchool) {
            this.graduateSchool = graduateSchool;
            if (graduateSchool != null) {
                this.graduateSchoolFlag = BaseDataUtil.getSchoolTypeFlag(graduateSchool);
            }
        }
    }
}
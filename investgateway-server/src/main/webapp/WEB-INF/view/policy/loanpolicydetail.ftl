<#import "../common.ftl" as common>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
<@common.includeCommonCss/>
<#--<link rel="stylesheet" href="/res/css/usersysloanpolicy.css">-->
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<div class="layui-colla-content layui-show">
    <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>表格中已包含所有策略详情项</div>
    <table class="layui-table" id="loanPolicyDetailTable" lay-size="sm">
        <thead>
        <tr>
            <th lay-data="{width:100}">属性名称</th>
            <th lay-data="{width:150, sort:true}">属性值</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>策略名称</td>
            <td>${loanPolicy.name!}</td>
        </tr>
        <tr>
            <td>策略编号</td>
            <td>${loanPolicy.id!}</td>
        </tr>
        <tr>
            <td>借款金额</td>
            <td>${loanPolicy.amount!}</td>
        </tr>
        <tr>
            <td>期限</td>
            <td>${loanPolicy.month!}</td>
        </tr>
        <tr>
            <td>利率</td>
            <td>${loanPolicy.rate!}</td>
        </tr>
        <tr>
            <td>魔镜等级</td>
            <td>${loanPolicy.creditCode!}</td>
        </tr>
        <tr>
            <td>借款人年龄</td>
            <td>${loanPolicy.age!}</td>
        </tr>
        <tr>
            <td>借款人性别</td>
            <td>${loanPolicy.sex!}</td>
        </tr>
        <tr>
            <td>第三方认证</td>
            <td>${loanPolicy.thirdAuthInfo!}</td>
        </tr>
        <tr>
            <td>学历认证</td>
            <td>${loanPolicy.certificate!}</td>
        </tr>
        <tr>
            <td>学习形式</td>
            <td>${loanPolicy.studyStyle!}</td>
        </tr>
        <tr>
            <td>毕业学校分类</td>
            <td>${loanPolicy.graduateSchoolType!}</td>
        </tr>
        <tr>
            <td>成功借款次数</td>
            <td>${loanPolicy.loanerSuccessCount!}</td>
        </tr>
        <tr>
            <td>流标次数</td>
            <td>${loanPolicy.wasteCount!}</td>
        </tr>
        <tr>
            <td>正常还款次数</td>
            <td>${loanPolicy.normalCount!}</td>
        </tr>
        <tr>
            <td>逾期(1-15)还清次数</td>
            <td>${loanPolicy.overdueLessCount!}</td>
        </tr>
        <tr>
            <td>逾期(15天以上)还清次数</td>
            <td>${loanPolicy.overdueMoreCount!}</td>
        </tr>
        <tr>
            <td>累计借款金额</td>
            <td>${loanPolicy.totalPrincipal!}</td>
        </tr>
        <tr>
            <td>待还金额</td>
            <td>${loanPolicy.owingPrincipal!}</td>
        </tr>
        <tr>
            <td>待收金额</td>
            <td>${loanPolicy.amountToReceive!}</td>
        </tr>
        <tr>
            <td>距最后一次借款成功天数</td>
            <td>${loanPolicy.lastSuccessBorrowDays!}</td>
        </tr>
        <tr>
            <td>本次借款距注册时间月数</td>
            <td>${loanPolicy.registerBorrowMonths!}</td>
        </tr>
        <tr>
            <td>待还金额/历史最高负债</td>
            <td>${loanPolicy.owingHighestDebtRatio!}</td>
        </tr>
        <tr>
            <td>本次借款/历史最高负债</td>
            <td>${loanPolicy.amtDebtRat!}</td>
        </tr>
        <tr>
            <td>创建时间</td>
            <td>${loanPolicy.createTime!}</td>
        </tr>

        </tbody>
    </table>
</div>

<@common.includeCommonJs/>
<script src="/res/mods/loanpolicydetail.js"></script>

</body>

</html>

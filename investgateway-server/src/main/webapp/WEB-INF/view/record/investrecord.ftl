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
    <link rel="stylesheet" href="/res/css/record/investrecord.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            <i class="iconfont icon-tags" style="margin-right: 5px;"></i><b>温馨提示:</b>
            <span>可根据详细条件查询您绑定的第三方账户的投资记录，可选择子策略加快定位查询，如果您未设置过子策略或子策略未生效则查询不到记录。</span>
        </div>
    </div>
    <div class="layui-inline">
        <form class="layui-form" action="" style="myform">
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <select name="thirdUserUUIDSelect" lay-filter="thirdUserUUIDSelectFilter" id="thirdUserUUIDSelect" lay-search="" class="third-user-uuid-select">
                        <option value="">第三方账号</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <select name="policyTypeSelect" lay-filter="policyTypeSelectFilter" id="policyTypeSelect" lay-search="" class="policy-type-select">
                        <option value="">策略类型</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="bidLoanBeginTime" placeholder="投标的起始时间">
            </div>
            <div class="layui-inline">
                <input type="text" class="layui-input" id="bidLoanEndTime" placeholder="投标的结束时间">
            </div>
            <div class="layui-inline">
                <a class="layui-btn layui-btn-normal" id="searchInvestRecord-btn">查询投资记录</a>
            </div>
        </form>
    </div>

</blockquote>

<table class="layui-table" id="investRecordTable"></table>

<@common.includeCommonJs/>
<script src="/res/mods/record/investrecord.js"></script>

<script type="text/html" id="optTpl">
    <a class="layui-btn layui-btn-normal layui-btn-mini search-invest-detail" style="background-color:#333333" data-id="{{d.id}}"><i class="iconfont icon-iconfontkucunchaxun01" style="margin-right: 5px;"></i>标的详情</a>
    <a class="third-user-uuid" style="display: none;">{{d.username}}</a>
    <a class="loanId" style="display:none;" data-id="{{d.loanId}}"></a>
</script>

<script type="text/html" id="loanListingDetailLinkTpl">
    <!-- 借款列表详情 -->
    <a class="loanListingDetail" target="_blank" href="http://www.ppdai.com/list/{{d.loanId}}" data-id="{{d.loanId}}">{{d.loanId}}</a>
</script>

</body>

</html>

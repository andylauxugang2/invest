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
    <link rel="stylesheet" href="/res/css/data/overdueanalysisdetail.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<input id="analysisIdData" value="${analysisId}" hidden="true"/>
<input id="overdueTypeData" value="${overdueType}" hidden="true"/>

<div class="layui-collapse">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title"><span style="float:left">逾期数据详情</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>历史数据分析(静态数据分析)</div></h2>
        <div class="layui-colla-content layui-show">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <div class="layui-form-mid layui-word-aux">
                        <i class="iconfont icon-tags" style="margin-right: 5px;"></i>
                        <a href="javascript:;">
                            <b>温馨提示</b>:投标时间在本月内的所有逾期标的列表。逾期详情每日更新，以下为历史数据分析，如想查看标的详情请点击标ID号跳转。
                        </a>
                    </div>
                </div>
            </blockquote>
            <table class="layui-table" id="overdueAnalysisDetailTable"></table>

        </div>
    </div>


<#--<div class="layui-colla-item">
    <h2 class="layui-colla-title"><span style="float:left">已挂载第三方账号</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>为第三方账号添加好的自定义散标列表,可直接启动/停止策略</div></h2>
    <div class="layui-colla-content layui-show">
        <table class="layui-table" id="userThirdUUIDLoanPolicyTable"></table>
    </div>
</div>-->

</div>

<@common.includeCommonJs/>
<script src="/res/mods/data/overdueanalysisdetail.js"></script>
</body>

</html>

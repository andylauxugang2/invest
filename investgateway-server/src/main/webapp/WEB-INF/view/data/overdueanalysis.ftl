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
    <link rel="stylesheet" href="/res/css/data/overdueanalysis.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<div class="layui-collapse">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title"><span style="float:left">逾期数据分析</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>历史数据分析(静态数据分析)</div></h2>
        <div class="layui-colla-content layui-show">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <div class="layui-form-mid layui-word-aux">
                        <i class="iconfont icon-tags" style="margin-right: 5px;"></i>
                        <a href="javascript:;">
                            <b>温馨提示</b>:标专家通过实时获取的散标详情、大数据量还款计划分析和投资记录分析，按期出逾期分析结果报表。逾期详情每日更新，以下为历史数据分析，本月数据请在实时数据里面分析中查看。
                        </a>
                    </div>
                </div>
                <div class="layui-inline">
                    <form class="layui-form" action="">
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <select name="thirdUserUUIDSelect" lay-filter="thirdUserUUIDSelectFilter" id="thirdUserUUIDSelect" lay-search="" class="third-user-uuid-select">
                                    <option value="">第三方账号</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-normal" id="searchOverdueAnalysis-btn">查询逾期统计数据</a>
                        </div>
                    </form>
                </div>
            </blockquote>
            <table class="layui-table" id="overdueAnalysisTable"></table>

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
<script src="/res/mods/data/overdueanalysis.js"></script>

<script type="text/html" id="overdueDetailLinkTpl">
    <a class="overdueDetail" style="color:#1E90FF;text-decoration:none;cursor:pointer" data-id="{{d.id}}" data-title="{{d.username}}&nbsp;&nbsp;{{d.month}}">{{d.username}}</a>
</script>

<script type="text/html" id="overdue10DetailLinkTpl">
    {{d.overdue10DaysRate}}&nbsp;(<a class="{{d.month=="总计"?"overdueDetail2":"overdueDetail"}}" data-id="{{d.id}}" data-overduetype="1" data-title="10天逾期率:{{d.username}}&nbsp;&nbsp;{{d.month}}">{{d.overdue10Days}}</a>/{{d.overdue10Total}})
</script>
<script type="text/html" id="overdue30DetailLinkTpl">
    {{d.overdue30DaysRate}}&nbsp;(<a class="{{d.month=="总计"?"overdueDetail2":"overdueDetail"}}" data-id="{{d.id}}" data-overduetype="2" data-title="30天逾期率:{{d.username}}&nbsp;&nbsp;{{d.month}}">{{d.overdue30Days}}</a>/{{d.overdue30Total}})
</script>
<script type="text/html" id="overdue60DetailLinkTpl">
    {{d.overdue60DaysRate}}&nbsp;(<a class="{{d.month=="总计"?"overdueDetail2":"overdueDetail"}}" data-id="{{d.id}}" data-overduetype="3" data-title="60天逾期率:{{d.username}}&nbsp;&nbsp;{{d.month}}">{{d.overdue60Days}}</a>/{{d.overdue60Total}})
</script>
<script type="text/html" id="overdue90DetailLinkTpl">
    {{d.overdue90DaysRate}}&nbsp;(<a class="{{d.month=="总计"?"overdueDetail2":"overdueDetail"}}" data-id="{{d.id}}" data-overduetype="4" data-title="90天逾期率:{{d.username}}&nbsp;&nbsp;{{d.month}}">{{d.overdue90Days}}</a>/{{d.overdue90Total}})
</script>

</body>

</html>

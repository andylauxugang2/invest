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
    <link rel="stylesheet" href="/res/css/addusersysloanpolicy.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<input id="thirdUserUUID" value="${thirdUserUUID}" hidden="true"/>

<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            <i class="iconfont icon-tags" style="margin-right: 5px;"></i><a href="javascript:;"><b>温馨提示</b>:标专家系统提供的系统级散标策略是众子策略的一种，操作比较简单，是标专家后台分析人员为用户提供的快速入门,快速选标的策略。
        </a>
        </div>
    </div>
    <div class="layui-inline">
        <form class="layui-form" action="">
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <select name="riskLevelSelect" lay-filter="riskLevelSelectFilter" id="riskLevelSelect" lay-search="" class="third-user-uuid-select">
                        <option value="">风险等级</option>
                    </select>
                </div>
            </div>
            <div class="layui-inline">
                <a class="layui-btn layui-btn-normal" id="searchMyPolicy-btn">筛选查询</a>
            </div>
            <div class="layui-inline">
                <a class="layui-btn" style="background-color:#5FB878" id="batchAddSysPolicy-btn">批量添加</a>
            </div>
        </form>
    </div>

</blockquote>

<table class="layui-table" id="sysLaonPolicyTable"></table>

<@common.includeCommonJs/>
<script src="/res/mods/addusersysloanpolicy.js"></script>

</body>

</html>

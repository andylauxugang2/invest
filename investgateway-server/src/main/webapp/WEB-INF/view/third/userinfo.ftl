<#import "../common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/third/userinfo.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>


<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            标专家通过授权后可获取您手机号绑定的所有拍拍贷第三方账户基本信息,如拍拍贷的账户余额信息,收益率,坏账率等。
        </div>
    </div>
</blockquote>

<table class="layui-table" id="thirdUserInfoTable"></table>

<@common.includeCommonJs/>
<script src="/res/mods/third/userinfo.js"></script>
</body>

</html>

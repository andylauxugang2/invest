<#import "./common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/authinfo.css">
</head>


<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            为保证用户数据的安全与隐私，标专家需要获取用户隐私数据，必须要获得用户的授权。授权会引导用户完成使用拍拍贷帐号“登录授权”的流程。该流程采用国际通用的OAuth2.0标准协议作为用户身份验证与授权协议，支持网站、M站授权。
        </div>
    </div>
    <div class="layui-inline">
        <a class="layui-btn gotoAuth" style="background-color:#5FB878">去授权</a>
    </div>
    <div class="layui-inline">
        <a class="layui-btn refresh" style="background-color:#333333">刷新页面</a>
    </div>
</blockquote>

<table class="layui-table" id="thirdAuthInfoTable"></table>

<@common.includeCommonJs/>
<script src="/res/mods/authinfo.js"></script>
<script type="text/html" id="thirdAuthStatusTpl">
    <input type="checkbox" name="show" lay-skin="switch" lay-text="是|否" lay-filter="isShow" {{d.userPolicyStatus==1?"checked":"no"}}/>
</script>
<script type="text/html" id="gotoThirdAuthTpl">
    <a class="layui-btn layui-btn-danger layui-btn-mini third_auth_del" data-id="{{d.id}}"><i class="iconfont icon-shanchu" style="margin-right: 5px;"></i>解绑授权</a>
</script>

</body>

</html>

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
    <link rel="stylesheet" href="/res/css/user/resetpwd.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<form method="post" class="layui-form">
    <div class="layui-form-item">
        <input type="text" name="userId" class="layui-input" autocomplete="off" lay-verify="required|number"
               value="${userId}" style="display: none">
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: 120px;">用户名</label>
        <div class="layui-input-inline">
            <input type="text" value="${user.mobile!}" disabled="" class="layui-input layui-disabled">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: 120px;">原密码</label>
        <div class="layui-input-inline">
            <input type="password" value="" placeholder="请输入您的原密码" lay-verify="required|password"
                   class="layui-input" name="password">
        </div>
        <div class="layui-form-mid layui-word-aux">您的原始登录密码</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: 120px;">新密码</label>
        <div class="layui-input-inline">
            <input type="password" value="" placeholder="请输入6-20位字母与数字的组合" lay-verify="required|newPassword"
                   class="layui-input" name="newPassword">
        </div>
        <div class="layui-form-mid layui-word-aux">您的新登录密码</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label" style="width: 120px;">再次输入新密码</label>
        <div class="layui-input-inline">
            <input type="password" value="" placeholder="请与新密码一致" lay-verify="required|newPassword2"
                   class="layui-input" name="newPassword2">
        </div>
        <div class="layui-form-mid layui-word-aux">重新输入您的新登录密码,请确保两次输入一致</div>
    </div>
    <div class="layui-form-item" style="margin-left: 5%;">
        <div class="layui-input-inline">
            <button class="layui-btn" lay-submit="" lay-filter="resetPwd">立即修改</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<@common.includeCommonJs/>
<script src="/res/mods/user/resetpwd.js"></script>
</body>

</html>

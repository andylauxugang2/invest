<#import "./common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta name="baidu-site-verification" content="bSiLKeOhic" />
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
    <meta name="renderer" content="webkit">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/login.css">
    <link id="layuicss-skinlayercss" rel="stylesheet" href="/res/css/layer.css" media="all">

    <style id="style-1-cropbar-clipper">
        .en-markup-crop-options {
            top: 18px !important;
            left: 50% !important;
            margin-left: -100px !important;
            width: 200px !important;
            border: 2px rgba(255, 255, 255, .38) solid !important;
            border-radius: 4px !important;
        }

        .en-markup-crop-options div div:first-of-type {
            margin-left: 0px !important;
        }
    </style>
</head>
<body>
<#--<video class="video-player" preload="auto" autoplay="autoplay" loop="loop" data-height="1080" data-width="1920"
       height="1080" width="1920" style="height: 591px; width: auto; left: -320px;">
    <source src="res/video/login.mp4" type="video/mp4">
</video>-->
<div class="video_mask"></div>
<div class="login">
    <h1>标专家登录</h1>

    <form method="post" class="layui-form">
        <div class="layui-form-item">
            <input class="layui-input" name="username" placeholder="用户名 请填写注册手机号" lay-verify="required" type="text"
                   autocomplete="off">
        </div>
        <div class="layui-form-item">
            <input class="layui-input" name="pass" placeholder="密码" lay-verify="required" type="password"
                   autocomplete="off">
        </div>
        <#--<div class="layui-form-item form_code">
            <input class="layui-input" name="checkCode" placeholder="验证码" lay-verify="required" type="text"
                   autocomplete="off">

            <div class="code"><img src="res/images/code.jpg" width="116" height="36"></div>
        </div>-->
        <button class="layui-btn login_btn" lay-submit lay-filter="loginfilter">登录</button>

        <div class="layui-form-item">
            <div class="new-user">
                <a href="/register">新用户注册</a>
                <a href="/forgetPassword">忘记密码?</a>
            </div>
        </div>
    </form>
</div>

<@common.includeCommonJs/>
<script src="/res/mods/login.js"></script>
</body>
</html>


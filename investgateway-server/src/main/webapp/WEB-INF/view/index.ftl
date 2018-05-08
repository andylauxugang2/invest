<#import "./common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta name="baidu-site-verification" content="bSiLKeOhic" />
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
    <@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/index.css">
</head>

<body>
<div class="layui-layout layui-layout-admin">
    <#include "./top.ftl"/>
    <!--左侧导航开始-->
    <#include "./left.ftl"/>
    <!-- 右侧主体 -->
    <div class="layui-body andy-body" id="andy-body">
        <div style="background: #1AA094;height: 10px"></div>
        <div class="layui-tab layui-tab-card andy-tab-box" id="andy-tab" lay-filter="tab" lay-allowClose="true">
            <ul class="layui-tab-title">
                <li class="layui-this" id="admin-home"><em class="iconfont icon-diannao1" style="margin-right: 5px;"></em>标专家首页</li>
            </ul>
            <div class="layui-tab-content" style="height: 100px;padding:5px;">
                <div class="layui-tab-item layui-show">
                    <iframe style="width: 100%;border: 0px;height: 100%;" data-id='0' src="/mainview?userId=${user.id}"></iframe>
                </div>
            </div>
        </div>
    </div>
    <!-- 底部区域 -->
    <div class="layui-footer layui-larry-foot" id="andy-footer">
        <p><a href="http://www.17ppd.com/">捉宝网-标专家</a> 2017 &copy; <a href="http://www.17ppd.com/">17ppd.com</a></p>
        <p>
            <a href="http://www.17ppd.com/auth/get" target="_blank">产品授权</a>
            <a href="http://www.17ppd.com/jie/8157.html" target="_blank">获取捉宝网</a>
            <a href="http://www.17ppd.com/jie/2461.html" target="_blank">微信公众号</a>
        </p>
    </div>

</div>

<@common.includeCommonJs/>
<script src="/res/mods/index.js"></script>
<script src="/res/mods/navtab.js"></script>
</body>
</html>

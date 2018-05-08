<#import "../common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/mainpolicy.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            <b>温馨提示</b>:标专家主策略是负责所有子策略的总开关,默认不开启,新用户注册请主动去选择并开启主策略。使用提供的散标,债权,跟踪和自定义等策略需要先添加并开启主策略。如果关闭主策略请点击"设置"按钮去完成关闭。
        </div>
    </div>
    <#--<div class="layui-inline">
        <a class="layui-btn refresh" style="background-color:#333333">刷新页面</a>
    </div>-->
    <div class="layui-inline">
        <form class="layui-form" action="" style="myform">
            <div class="layui-inline">
                <label class="layui-form-label" style="width:200px;">请选择要设置的第三方账号</label>
                <div class="layui-input-inline">
                    <select name="thirdUserUUIDSelect" lay-filter="thirdUserUUIDSelectFilter" id="thirdUserUUIDSelect" style="font: 12px">
                        <#--<option value="">全部</option>
                        <option value="1">layer</option>
                        <option value="2">form</option>
                        <option value="3">layim</option>
                        <option value="4">element</option>
                        <option value="5">laytpl</option>
                        <option value="8">laypage</option>-->
                    </select>
                </div>
            </div>
        </form>
    </div>
</blockquote>

<table class="layui-table" id="mainPolicyTable"></table>

<@common.includeCommonJs/>
<script src="/res/mods/mainpolicy.js"></script>

<script type="text/html" id="setMainPolicyTpl">
    <a class="layui-btn layui-btn-normal layui-btn-mini main_policy_set" data-id="{{d.mainPolicyId}}"><i class="iconfont icon-shezhi" style="margin-right: 5px;"></i>设置主策略</a>
</script>
<script type="text/html" id="mainPolicyStatusTpl">
    <input type="checkbox" name="show" lay-skin="switch" lay-text="是|否" lay-filter="isShow" disabled {{d.userPolicyStatus==1?"checked":"no"}}/>
</script>

</body>

</html>

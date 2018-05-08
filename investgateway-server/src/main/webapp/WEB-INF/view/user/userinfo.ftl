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
    <link rel="stylesheet" href="/res/css/user/setuserinfo.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<form method="post" class="layui-form">
    <div class="user_left">
        <div class="layui-form-item">
            <input type="text" name="userId" class="layui-input" autocomplete="off" lay-verify="required|number"
                   value="${userId}" style="display: none">
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>

            <div class="layui-input-block">
                <input type="text" value="${user.mobile!}" disabled="" class="layui-input layui-disabled">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">上次登录时间</label>

            <div class="layui-input-block">
                <input type="text" value="2017-08-21 12:20:12" disabled="" placeholder="请输入个性化昵称" lay-verify="required"
                       class="layui-input nick">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">昵称</label>

            <div class="layui-input-block">
                <input type="text" value="${user.nick!}" placeholder="请输入个性化昵称" lay-verify="required|nick"
                       class="layui-input" name="nick">
            </div>
        </div>
        <div class="layui-form-item" style="margin-left: 5%;">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="setUser">保存设置</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </div>
    <div class="user_right">
        <div class="layui-box layui-upload-button">
            <#--<form method="post" key="set-mine" enctype="multipart/form-data">
                <input type="file" name="userFace" class="layui-upload-file" lay-title="头像不满意？去换一个头像">
                <input name="userId" value="${userId}" hidden="true"/>
            </form>-->
            <button type="button" class="layui-btn" id="changeHeadImgBtn">
                <i class="layui-icon">&#xe67c;</i>头像不满意？去换一个头像
            </button>
        <#--<span class="layui-upload-icon"><i class="layui-icon"></i>我要换一个头像</span>-->
        </div>
        <p>注意:图片大小不要超过2M。仅支持文件后缀为png、jpg、gif的图片</p>
    <#if user.headImg?? && user.headImg!="">
        <img src="data:image/png;base64,${user.headImg}" class="layui-circle" id="userFace">
    <#else>
        <img src="/res/images/face.jpg" class="layui-circle" id="userFace">
    </#if>

    </div>
</form>
<@common.includeCommonJs/>
<script src="/res/mods/user/setuserinfo.js"></script>
</body>

</html>

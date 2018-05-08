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
    <link rel="stylesheet" href="/res/css/message/usernotifymessage.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<div class="layui-collapse">
    <div class="layui-colla-item">
        <div class="layui-colla-content layui-show">
            <div class="andy-panel andy-panel-user" style="padding: 20px 120px;">
                <div style="text-align: center;font-size: 16px;">${notifyMessage.title!}</div>
                <div style="text-align: center;font-size: 13px;">${notifyMessage.createTimeFormat!}</div>
                <hr class="layui-bg-orange">
                <table class="layui-table" id="userSysLaonPolicyTable"></table>
                <div style="text-align: left">
                    <span style="font-size: 15px;">尊敬的</span>
                    <span style="font: 14px;color:blue;">${mobile!}</span>
                    <span style="font-size: 15px;">：</span>
                </div>
                <div style="padding-left: 30px;">
                ${notifyMessage.content!}
                </div>
            </div>
        </div>
    </div>

</div>

<@common.includeCommonJs/>

</body>

</html>

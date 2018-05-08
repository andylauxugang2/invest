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

<div class="layui-tab layui-tab-brief" lay-filter="messageTabFilter">
    <blockquote class="layui-elem-quote news_search">
        <div class="layui-inline">
            <div class="layui-form-mid layui-word-aux">
                点击标题可查看消息内容详情，根据内容中的提示可以帮助您解决一些问题哦。
            </div>
        </div>
    </blockquote>

    <ul class="layui-tab-title">
        <li class="layui-this">未读消息</li>
        <li>已读消息</li>
        <li>全部消息</li>
    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show">
            <div class="layui-collapse">
                <div class="layui-colla-item">
                    <div class="layui-colla-content layui-show">
                        <div class="layui-inline">
                            <form class="layui-form">
                                <div class="layui-inline">
                                    <a class="layui-btn layui-btn-sm layui-btn-radius" style="" id="doRead-btn">全部已读</a>
                                </div>
                            </form>
                        </div>
                        <table class="layui-table" id="unReadMessageTable"></table>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-tab-item">
            <div class="layui-collapse">
                <div class="layui-colla-item">
                    <div class="layui-colla-content layui-show">
                        <table class="layui-table" id="readedMessageTable"></table>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-tab-item">
            <div class="layui-collapse">
                <div class="layui-colla-item">
                    <div class="layui-colla-content layui-show">
                        <table class="layui-table" id="allMessageTable"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<@common.includeCommonJs/>
<script src="/res/mods/message/usernotifymessage.js"></script>

<script type="text/html" id="titleTpl">
    {{d.status==0?"<span class='layui-badge-dot' style='margin-right: 5px;'></span>":""}}
    <a class="read-message" data-href="{{d.link}}" data-id="{{d.id}}" data-messageId="{{d.messageId}}" style="text-decoration:none;cursor:pointer" data-status="{{d.status}}">{{d.title}}</a>
</script>

</body>

</html>

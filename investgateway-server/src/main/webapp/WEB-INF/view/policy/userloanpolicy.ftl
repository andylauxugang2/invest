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
    <link rel="stylesheet" href="/res/css/userloanpolicy.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<div class="layui-collapse">
    <div class="layui-colla-item">
        <h2 class="layui-colla-title"><span style="float:left">自定义散标策略列表</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>用户添加的自定义散标策略仓库</div></h2>
        <div class="layui-colla-content layui-show">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <div class="layui-form-mid layui-word-aux">
                        <i class="iconfont icon-tags" style="margin-right: 5px;"></i>
                        <a href="javascript:;">
                            <b>温馨提示</b>:标专家除了提供系统级散标策略外，允许用户自定义散标策略，操作比较简单，属于子策略的一种。请先开启主策略——散标自定义。
                        </a>
                    </div>
                </div>
                <div class="layui-inline">
                    <form class="layui-form" action="" style="myform">
                        <div class="layui-inline">
                            <a class="layui-btn recommend" style="background-color:#5FB878" id="addMyPolicy-btn">添加自定义</a>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-danger" id="delMyPolicy-btn">批量删除自定义</a>
                        </div>
                    </form>
                </div>

            </blockquote>

            <table class="layui-table" id="userLoanPolicyTable"></table>

        </div>
    </div>


    <div class="layui-colla-item">
        <h2 class="layui-colla-title"><span style="float:left">已挂载第三方账号</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>为第三方账号添加好的自定义散标列表,可直接启动/停止策略</div></h2>
        <div class="layui-colla-content layui-show">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <form class="layui-form" action="" style="myform">
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <select name="thirdUserUUIDSelect" lay-filter="thirdUserUUIDSelectFilter" id="thirdUserUUIDSelect" lay-search="" class="third-user-uuid-select">
                                    <option value="">请先选择第三方账号</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-danger" id="dettachMyPolicy-btn">解挂</a>
                        </div>
                    </form>
                </div>
            </blockquote>

            <table class="layui-table" id="userThirdUUIDLoanPolicyTable"></table>

        </div>
    </div>


</div>

<@common.includeCommonJs/>
<script src="/res/mods/userloanpolicy.js"></script>

<script type="text/html" id="optTpl">
    <a class="layui-btn layui-btn-normal layui-btn-mini save-user-policy-modify" style="background-color:#333333" data-id="{{d.id}}"><i class="iconfont icon-shujuku" style="margin-right: 5px;"></i>修改</a>
    <a class="layui-btn layui-btn-normal layui-btn-mini attach-loan-policy" style="background-color:#333333" data-id="{{d.id}}"><i class="iconfont icon-shujuku" style="margin-right: 5px;"></i>挂载</a>
    <a class="policy-id" style="display:none;" data-id="{{d.policyId}}"></a>
</script>



<script type="text/html" id="userThirdOptTpl">
    <a class="layui-btn layui-btn-normal layui-btn-mini save-policy-modify" style="background-color:#333333" data-id="{{d.userPolicyId}}"><i class="iconfont icon-shujuku" style="margin-right: 5px;"></i>保存修改</a>
    <#--<a class="layui-btn layui-btn-normal layui-btn-mini search-bid-records" style="background-color:#333333" data-id="{{d.userPolicyId}}"><i class="iconfont icon-iconfontkucunchaxun01" style="margin-right: 5px;"></i>投标记录</a>-->
    <a class="status-is-show" style="display: none;">{{d.userPolicyStatus}}</a>
    <a class="third-user-uuid" style="display: none;">{{d.username}}</a>
    <a class="policy-id" style="display:none;" data-id="{{d.id}}"></a>
</script>
<script type="text/html" id="bidAmountTpl">
    <input type="text" class="bid-amount" name="bidAmount" value="{{d.bidAmount}}" style="width: 50px;">
</script>
<script type="text/html" id="policyStatusTpl">
    <input type="checkbox" name="show" lay-skin="switch" lay-text="on|off" lay-filter="isShow" {{d.userPolicyStatus==1?"checked":"no"}}/>
</script>

<script type="text/html" id="policyDetailLinkTpl">
    <a class="policyDetail" style="color:#1E90FF;text-decoration:none;cursor:pointer" data-id="{{d.id}}">{{d.name}}</a>
</script>

</body>

</html>

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
    <link rel="stylesheet" href="/res/css/usersysloanpolicy.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<div class="layui-collapse">
    <div class="layui-colla-item">
        <h3 class="layui-colla-title"><span style="float:left">系统推荐散标策略列表</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>自定义散标策略投标顺序高于系统散标策略</div></h3>
        <div class="layui-colla-content layui-show">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <div class="layui-form-mid layui-word-aux">
                        <i class="iconfont icon-tags" style="margin-right: 5px;"></i><a href="javascript:;"><b>温馨提示</b>:标专家系统提供的系统级散标策略是众子策略的一种，操作比较简单，是标专家后台分析人员为用户提供的快速入门,快速选标的策略。
                    </a>
                    </div>
                </div>
                <div class="layui-inline">
                    <form class="layui-form" action="" style="myform">
                        <div class="layui-inline">
                        <#--<label class="layui-form-label"  style="width:120px;">请先选择第三方账号</label>-->
                            <div class="layui-input-inline">
                                <select name="thirdUserUUIDSelect" lay-filter="thirdUserUUIDSelectFilter" id="thirdUserUUIDSelect" lay-search="" class="third-user-uuid-select">
                                    <option value="">请先选择第三方账号</option>
                                </select>
                            </div>
                        </div>
                        <#--<div class="layui-inline">
                            <div class="layui-input-inline">
                                <select name="riskLevelSelect" lay-filter="riskLevelSelectFilter" id="riskLevelSelect" lay-search="" class="third-user-uuid-select">
                                    <option value="">请选择策略风险等级</option>
                                </select>
                            </div>
                        </div>-->

                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-normal" id="searchMyPolicy-btn">查询我的策略</a>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn recommend" style="background-color:#5FB878" id="addMyPolicy-btn">添加新策略</a>
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-danger" id="delMyPolicy-btn">删除我的策略</a>
                        </div>
                    </form>
                </div>

            </blockquote>
            <table class="layui-table" id="userSysLaonPolicyTable"></table>
        </div>
    </div>

</div>

<@common.includeCommonJs/>
<script src="/res/mods/usersysloanpolicy.js"></script>

<script type="text/html" id="optTpl">
    <a class="layui-btn layui-btn-normal layui-btn-mini save-policy-modify" style="background-color:#333333" data-id="{{d.userPolicyId}}"><i class="iconfont icon-shujuku" style="margin-right: 5px;"></i>保存修改</a>
    <#--<a class="layui-btn layui-btn-normal layui-btn-mini search-bid-records" style="background-color:#333333" data-id="{{d.userPolicyId}}"><i class="iconfont icon-iconfontkucunchaxun01" style="margin-right: 5px;"></i>投标记录</a>-->
    <a class="status-is-show" style="display: none;">{{d.userPolicyStatus}}</a>
    <a class="third-user-uuid" style="display: none;">{{d.username}}</a>
    <a class="policy-id" style="display:none;" data-id="{{d.id}}"></a>
</script>
<script type="text/html" id="policyStatusTpl">
    <input type="checkbox" name="show" lay-skin="switch" lay-text="on|off" lay-filter="isShow" {{d.userPolicyStatus==1?"checked":"no"}}/>
</script>
<script type="text/html" id="bidAmountTpl">
    <input type="text" class="bid-amount" name="bidAmount" value="{{d.bidAmount}}" style="width: 50px;">
</script>
<script type="text/html" id="policyDetailLinkTpl">
    <a class={{d.riskLevelCode!=4?"policyDetail":""}} style="color:#1E90FF;text-decoration:none;cursor:pointer" data-id="{{d.id}}">{{d.name}}</a>
</script>
</body>

</html>

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
    <link rel="stylesheet" href="/res/css/setmainpolicy.css">
</head>

<body class="childrenBody">

<form method="post" class="layui-form" style="width:80%;margin-top:10px;">
    <div class="layui-form-item">
        <div class="layui-input-inline">
            <input type="text" name="mainPolicyId" class="layui-input" autocomplete="off" lay-verify="required|number"
                   value="${mainPolicyId}" style="display: none">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-inline">
            <input type="text" name="userId" class="layui-input" autocomplete="off" lay-verify="required|number"
                   value="${userId}" style="display: none">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-inline">
            <input type="text" name="thirdUserUUID" class="layui-input" autocomplete="off" lay-verify="required"
                   value="${thirdUserUUID}" style="display: none">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">主策略编号</label>
        <div class="layui-input-inline">
            <input class="layui-input" name="userMainPolicyId" value="${userMainPolicyVO.userPolicyId!}" disabled/>
            <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>提示:标专家在您保存设置后生成唯一主策略编号,用于排查问题使用</div>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">标专家账号</label>
        <div class="layui-input-inline">
            <input class="layui-input" value="${mobile}" disabled/>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">第三方账号</label>
        <div class="layui-input-inline">
            <input class="layui-input" value="${thirdUserUUID!}" disabled/>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">起步投标金额</label>

        <#--<div class="layui-input-block">-->
        <div class="layui-input-inline">
            <input type="text" name="amountStart" class="layui-input" autocomplete="off" lay-verify="required|number|amountStart"
                   placeholder="请输入起步投标金额,纯数字类型" value="${userMainPolicyVO.amountStart!}">
        </div>
        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>提示:标专家提供的子策略投标金额必须大于或等于起步金额才会自动投标</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">最大投标金额</label>

        <div class="layui-input-inline">
            <input type="text" name="amountMax" class="layui-input" autocomplete="off" lay-verify="required|number|amountMax"
                   placeholder="请输入最大投标金额,纯数字类型" value="${userMainPolicyVO.amountMax!}">
        </div>
        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>提示:标专家为管家设置每标投出的最大金额，所属子策略均受最大投标金额控制</div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">账户保留金额</label>

        <div class="layui-input-inline">
            <input type="text" name="accountRemain" class="layui-input" autocomplete="off" lay-verify="required|number|accountRemain"
                   placeholder="请输入账户保留金额,纯数字类型" value="${userMainPolicyVO.accountRemain!}">
        </div>
        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>提示:在自动投标运行时会为管家第三方帐号金额保留，实时保留计算</div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">开启状态</label>

            <div class="layui-input-inline">
                <!-- 设置value="1"可自定义值，否则选中时返回的就是默认的on -->
                <input type="checkbox" name="status" lay-skin="switch" lay-text="开启|关闭" value="1" <#if userMainPolicyVO.userPolicyStatus?? && userMainPolicyVO.userPolicyStatus==1>checked</#if>>
            </div>
            <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>提示:开启主策略,默认其子策略全部开启,可通过设置子策略开关来控制细粒度投标</div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="addUserMainPolicy">保存设置</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<@common.includeCommonJs/>
<script src="/res/mods/setmainpolicy.js"></script>
</body>

</html>

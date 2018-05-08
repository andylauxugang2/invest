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
    <link rel="stylesheet" href="/res/css/adduserloanpolicy.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<blockquote class="layui-elem-quote news_search">
    <div class="layui-inline">
        <div class="layui-form-mid layui-word-aux">
            <i class="iconfont icon-tags" style="margin-right: 5px;"></i>
            <a href="javascript:;">
                <b>温馨提示</b>:标专家除了提供系统级散标策略外，允许用户自定义散标策略，操作比较简单，属于子策略的一种。请先开启主策略——散标自定义。注意:以下选项不填表示无限制。
            </a>
        </div>
    </div>
</blockquote>

<fieldset class="layui-elem-field layui-field-title">
    <legend>散标自定义面板</legend>
</fieldset>

<form method="post" class="layui-form">
<#--<div class="layui-form-item">
    <div class="layui-input-inline">
        <input type="text" name="mainPolicyId" class="layui-input" autocomplete="off" lay-verify="number"
               value="${mainPolicyId}" style="display: none">
    </div>
</div>-->
    <div class="layui-collapse">
        <div class="layui-colla-item">
            <div class="layui-colla-content layui-show">
            <#--<div class="layui-form-item">
                <label class="layui-form-label">应用第三方账号</label>
                <div class="layui-input-block userThirdBindInfoList">
                <#list userThirdBindInfoList as userThirdBindInfo>
                    <input type="checkbox" name="userThirdBindInfoList" title="${userThirdBindInfo.thirdUserUUID}" value="${userThirdBindInfo.thirdUserUUID}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                </#list>
                </div>
                <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>该策略要被应用到的第三方授权用户列表</div>
            </div>-->
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">策略名称</label>
                        <div class="layui-input-inline">
                            <input type="text" name="userPolicyName" lay-verify="required|userPolicyName" placeholder="请输入策略中文名称"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.name!}">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>标专家不允许策略中文名重复</div>
                    </div>
                </div>
            <#--<div class="layui-form-item">
                <label class="layui-form-label">单笔投资金额</label>
                <div class="layui-input-inline" style="width: 30%;">
                    <input type="text" name="bidAmount" lay-verify="bidAmount" placeholder="￥"
                           autocomplete="off" class="layui-input" value="${loanPolicy.name!}">
                    <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>子策略投标金额会覆盖主策略投标金额,最低50起投</div>
                </div>
            </div>-->
            </div>
        </div>

        <div class="layui-colla-item">
            <h2 class="layui-colla-title"><span style="float:left">借款基本信息</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>包括借款金额、借款期限以及利率等基本借款信息</div></h2>
            <div class="layui-colla-content layui-show">

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <input type="text" name="id" id="userLoanPolicyId" class="layui-input" style="display: none;" value="${loanPolicy.id!}">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">期限</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="monthBegin" lay-verify="monthBegin" placeholder="起始月份"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.monthBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="monthEnd" lay-verify="monthEnd" placeholder="截止月份"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.monthEnd!}">
                        </div>
                        <div class="layui-form-mid">月</div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>请填写36以内的月份</div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">利率</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="rateBegin" lay-verify="rateBegin" placeholder="起始利率"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.rateBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="rateEnd" lay-verify="rateEnd" placeholder="截止利率"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.rateEnd!}">
                        </div>
                        <div class="layui-form-mid">%</div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>请填写1-100以内的数字,支持小数</div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">借款金额</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="amountBegin" lay-verify="amountBegin" placeholder="￥"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="amountEnd" lay-verify="amountEnd" placeholder="￥"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountEnd!}">
                        </div>
                        <div class="layui-form-mid">元</div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>请填写整数金额,示例:起始金额填0,截止金额填5000,表示:0<=散标投资金额<=5000</div>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">魔镜等级</label>
                    <div class="layui-input-block creditCode">
                    <#--创建一个map,注意在freemarker中,map的key只能是字符串来作为key-->
                    <#assign thisCreditCodeMap=creditCodeMap/>
                    <#--获取map的keys-->
                    <#assign  keys=thisCreditCodeMap?keys/>
                    <#--遍历map 首选获取key的集合-->
                    <#list keys as key>
                        <#assign data=thisCreditCodeMap["${key}"]?eval/>
                        <input <#if data.checked?? && data.checked==true>checked</#if> type="checkbox" name="creditCode" title='${data.desc}' value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>
                    <#--直接遍历map的第二种方式-->
                    <#--<#list thisCreditCodeMap?keys as key>
                        key:${key}--value:${thisCreditCodeMap["${key}"]}
                    </#list>-->
                    <#--直接遍历map的values-->
                    <#--<#list thisCreditCodeMap?values as value>
                    ${value}
                    </#list>-->

                    <#--<#list map?keys as key>
                        ${map.get(key)}
                        <input type="checkbox" name="creditCode" title="1" value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>-->
                    </div>
                    <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>可多选,如全未勾选,表示此策略无限制</div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">借款专项</label>
                    <div class="layui-input-block">
                        <input type="checkbox" name="xxx" title="闪电" value="1"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                        <input type="checkbox" name="xxx" title="APP借款" value="1"><div class="layui-unselect layui-form-checkbox layui-form-checked" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </div>
                </div>

            </div>
        </div>

        <div class="layui-colla-item">
            <h2 class="layui-colla-title"><span style="float:left">借款人信息</span><div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>包括年龄、性别、学习形式和认证、学校分类等借款人基本信息</div></h2>
            <div class="layui-colla-content layui-show">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">年龄</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="ageBegin" lay-verify="ageBegin" placeholder="起始年龄"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.ageBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="ageEnd" lay-verify="ageEnd" placeholder="最大年龄"
                                   autocomplete="off" class="layui-input" value="${loanPolicy.ageEnd!}">
                        </div>
                        <div class="layui-form-mid">岁</div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>年龄最大不可超过150正常人年龄</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">性别</label>
                    <div class="layui-input-block sex">
                        <input type="radio" name="sex" value="1" title="男" <#if loanPolicy.sex?? && loanPolicy.sex==1>checked</#if>><div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon"></i><span>男</span></div>
                        <input type="radio" name="sex" value="0" title="女" <#if loanPolicy.sex?? && loanPolicy.sex==0>checked</#if>><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><span>女</span></div>
                        <input type="radio" name="sex" value="" title="包含全部" <#if !loanPolicy.sex??>checked</#if>><div class="layui-unselect layui-form-radio"><i class="layui-anim layui-icon"></i><span>包含全部</span></div>
                    </div>
                    <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>年龄最大不可超过150正常人年龄</div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">学历认证</label>
                    <div class="layui-input-block certificate">
                    <#assign thisCertificateMap=certificateMap/>
                    <#assign  keys=thisCertificateMap?keys/>
                    <#list keys as key>
                        <#assign data=thisCertificateMap["${key}"]?eval/>
                        <input <#if data.checked?? && data.checked==true>checked</#if> type="checkbox" name="certificate" title='${data.desc}' value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">学习形式</label>
                    <div class="layui-input-block studyStyle">
                    <#assign thisStudyStyleMap=studyStyleMap/>
                    <#assign  keys=thisStudyStyleMap?keys/>
                    <#list keys as key>
                        <#assign data=thisStudyStyleMap["${key}"]?eval/>
                        <input <#if data.checked?? && data.checked==true>checked</#if> type="checkbox" name="studyStyle" title='${data.desc}' value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">学校等级</label>
                    <div class="layui-input-block graduateSchoolType">
                    <#assign thisGraduateSchoolTypeMap=graduateSchoolTypeMap/>
                    <#assign  keys=thisGraduateSchoolTypeMap?keys/>
                    <#list keys as key>
                        <#assign data=thisGraduateSchoolTypeMap["${key}"]?eval/>
                        <input <#if data.checked?? && data.checked==true>checked</#if> type="checkbox" name="graduateSchoolType" title='${data.desc}' value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-colla-item">
            <h2 class="layui-colla-title">第三方认证信息</h2>
            <div class="layui-colla-content layui-show">
                <div class="layui-form-item">
                    <label class="layui-form-label">第三方认证</label>
                    <div class="layui-input-block thirdAuthInfo">
                    <#assign thisThirdAuthInfoMap=thirdAuthInfoMap/>
                    <#assign  keys=thisThirdAuthInfoMap?keys/>
                    <#list keys as key>
                        <#assign data=thisThirdAuthInfoMap["${key}"]?eval/>
                        <input <#if data.checked?? && data.checked==true>checked</#if> type="checkbox" name="thirdAuthInfo" title='${data.desc}' value="${key}"><div class="layui-unselect layui-form-checkbox" lay-skin=""><span></span><i class="layui-icon"></i></div>
                    </#list>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-colla-item">
            <h2 class="layui-colla-title">信用记录</h2>
            <div class="layui-colla-content layui-show">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">成功借款次数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="loanerSuccessCountBegin" lay-verify="loanerSuccessCountBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.loanerSuccessCountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="loanerSuccessCountEnd" lay-verify="loanerSuccessCountEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.loanerSuccessCountEnd!}">
                        </div>
                        <div class="layui-form-mid">次</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">流标次数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="wasteCountBegin" lay-verify="wasteCountBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.wasteCountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="wasteCountEnd" lay-verify="wasteCountEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.wasteCountEnd!}">
                        </div>
                        <div class="layui-form-mid">次</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">正常还款次数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="normalCountBegin" lay-verify="normalCountBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.normalCountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="normalCountEnd" lay-verify="normalCountEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.normalCountEnd!}">
                        </div>
                        <div class="layui-form-mid">次</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">逾期(1-15)还清次数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="overdueLessCountBegin" lay-verify="overdueLessCountBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.overdueLessCountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="overdueLessCountEnd" lay-verify="overdueLessCountEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.overdueLessCountEnd!}">
                        </div>
                        <div class="layui-form-mid">次</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">逾期(15天以上)还清次数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="overdueMoreCountBegin" lay-verify="overdueMoreCountBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.overdueMoreCountBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="overdueMoreCountEnd" lay-verify="overdueMoreCountEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.overdueMoreCountEnd!}">
                        </div>
                        <div class="layui-form-mid">次</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-colla-item">
            <h2 class="layui-colla-title">借款状态</h2>
            <div class="layui-colla-content layui-show">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">累计借款金额</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="totalPrincipalBegin" lay-verify="totalPrincipalBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.totalPrincipalBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="totalPrincipalEnd" lay-verify="totalPrincipalEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.totalPrincipalEnd!}">
                        </div>
                        <div class="layui-form-mid">元</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">待还金额</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="owingPrincipalBegin" lay-verify="owingPrincipalBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.owingPrincipalBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="owingPrincipalEnd" lay-verify="owingPrincipalEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.owingPrincipalEnd!}">
                        </div>
                        <div class="layui-form-mid">元</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">待收金额</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="amountToReceiveBegin" lay-verify="amountToReceiveBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountToReceiveBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="amountToReceiveEnd" lay-verify="amountToReceiveEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountToReceiveEnd!}">
                        </div>
                        <div class="layui-form-mid">元</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">借款总额度</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="amountOwingTotalBegin" lay-verify="amountOwingTotalBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountOwingTotalBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="amountOwingTotalEnd" lay-verify="amountOwingTotalEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amountOwingTotalEnd!}">
                        </div>
                        <div class="layui-form-mid">元</div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>如果此处总额度=20000元,且借款人的待还=4000元,那么如果他借16000元以下则系统会命中此策略并投标</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">距最后一次借款成功天数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="lastSuccessBorrowDaysBegin" lay-verify="lastSuccessBorrowDaysBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.lastSuccessBorrowDaysBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="lastSuccessBorrowDaysEnd" lay-verify="lastSuccessBorrowDaysEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.lastSuccessBorrowDaysEnd!}">
                        </div>
                        <div class="layui-form-mid">天</div>
                    </div>
                </div>
                <#--<div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">借款频率</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="borrowFrequencyBegin" lay-verify="borrowFrequencyBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.borrowFrequencyBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="borrowFrequencyEnd" lay-verify="borrowFrequencyEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.borrowFrequencyEnd!}">
                        </div>
                        <div class="layui-form-mid">月</div>
                    </div>
                </div>-->
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">本次借款距注册时间月数</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="registerBorrowMonthsBegin" lay-verify="registerBorrowMonthsBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.registerBorrowMonthsBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="registerBorrowMonthsEnd" lay-verify="registerBorrowMonthsEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.registerBorrowMonthsEnd!}">
                        </div>
                        <div class="layui-form-mid">月</div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-colla-item">
            <h2 class="layui-colla-title">高级指标设置</h2>
            <div class="layui-colla-content layui-show">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">待还金额/历史最高负债</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="owingHighestDebtRatioBegin" lay-verify="owingHighestDebtRatioBegin" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.owingHighestDebtRatioBegin!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="owingHighestDebtRatioEnd" lay-verify="owingHighestDebtRatioEnd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.owingHighestDebtRatioEnd!}">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>请填写0-1之间的小数,注意:如果此处设置值则不会投"首借"的标</div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">本次借款/历史最高负债</label>
                        <div class="layui-input-inline"  style="width: 100px;">
                            <input type="text" name="amtDebtRatBg" lay-verify="amtDebtRatBg" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amtDebtRatBg!}">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input type="text" name="amtDebtRatEd" lay-verify="amtDebtRatEd" placeholder=""
                                   autocomplete="off" class="layui-input" value="${loanPolicy.amtDebtRatEd!}">
                        </div>
                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>请填写0-1之间的小数,注意:如果此处设置值则不会投"首借"的标</div>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <div class="layui-form-item" style="margin-top: 10px;">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="addUserPolicy">保存设置</button>
        <#--<button type="reset" class="layui-btn layui-btn-primary">重置</button>-->
        </div>
    </div>
</form>
<@common.includeCommonJs/>
<script src="/res/mods/adduserloanpolicy.js"></script>

</body>

</html>

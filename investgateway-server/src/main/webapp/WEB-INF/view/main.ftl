<#import "common.ftl" as common>
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
    <!-- load css -->
    <link rel="stylesheet" type="text/css" href="/res/css/bootstrap.min.css" media="all">
    <link rel="stylesheet" href="/res/css/main.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>
<div class="larry-grid larry-wrapper">
    <div class="row" id="infoSwitch">
        <blockquote class="layui-elem-quote col-md-12 head-con">
            <div>Hi <span>${user.nick!} </span>！您的上次登录时间是:${user.lastLoginTimeFormat!}</div>
            <i class="iconfont icon-shujuku close" id="closeInfo"></i>
        </blockquote>
    </div>
    <div class="row shortcut" id="shortcut">
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel clearfix">
                <div class="symbol shortcut-bg1"><i class="iconfont icon-zhifu"></i></div>
                <div class="value">
                    <a data-href="/accountinfoview?userId=${userId}" data-title="我的账户" data-icon="icon-zhifu">
                        <h1 id="count1">${user.zhuobaoBalance!}</h1>
                    </a>
                    <p>捉宝币余额</p>
                </div>
            </section>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel">
                <div class="symbol shortcut-bg2"><i class="iconfont icon-chajian"></i></div>
                <div class="value">
                    <a data-href="/record/investrecordview?userId=${userId}" data-title="投资记录查询" data-icon="icon-chaxun1">
                        <h1 id="count2">${user.bidCountToday!}</h1>
                    </a>
                    <p>今日投标</p>
                </div>
            </section>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel">
                <div class="symbol shortcut-bg3"><i class="iconfont icon-zhifu"></i></div>
                <div class="value">
                    <a data-href="">
                        <h1 id="count3">${user.incomeToday!}</h1>
                    </a>

                    <p>今日收益</p>
                </div>
            </section>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel">
                <div class="symbol shortcut-bg4"><i class="iconfont icon-icon1"></i></div>
                <div class="value">
                    <a data-href="">
                        <h1 id="count4">${user.overdueTodayCount!}</h1>
                    </a>

                    <p>今日逾期</p>
                </div>
            </section>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel">
                <div class="symbol shortcut-bg5"><i class="iconfont icon-shujuku"></i></div>
                <div class="value">
                    <a data-href="/ivgatepolicy/usersysloanpolicyview?userId=${userId}" data-title="系统推荐散标" data-icon="icon-chajian">
                        <span class="smallnum"><span id="sysloanHover" class="layui-badge layui-bg-orange">系</span>${user.sysLoanPolicyCount!}</span>
                    </a>
                    <a data-href="/ivgatepolicy/userloanpolicyview?userId=${userId}" data-title="散标自定义" data-icon="icon-chajian">
                        <span class="smallnum"><span id="sysloanHover" class="layui-badge layui-bg-blue">自</span>${user.loanPolicyCount!}</span>
                    </a>
                    <a data-href="">
                        <span class="smallnum"><span id="sysloanHover" class="layui-badge layui-bg-green">跟</span>0</span>
                    </a>
                    <p>运行策略</p>
                </div>
            </section>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-4 col-lg-2 ">
            <section class="panel">
                <div class="symbol shortcut-bg6"><i class="iconfont icon-geren"></i></div>
                <div class="value">
                    <a data-href="/third/authview?userId=${userId}" data-title="拍拍贷用户授权" data-icon="icon-icon1">
                        <h1 id="count4">${user.bindUserCount!}</h1>
                    </a>

                    <p>绑定用户</p>
                </div>
            </section>
        </div>
    </div>
    <!-- 首页信息 -->
    <div class="row system">
        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
            <section class="panel">
                <header class="panel-heading bm0">
                    <span class="span-title">标专家介绍</span>
                    <span class="tools pull-right"><a href="javascript:;" class="iconpx-chevron-down"></a></span>
                </header>
                <div class="panel-body">
                    <div class="larry-table">
                        <table class="layui-table larry-table-info">
                            <colgroup>
                                <col width="150">
                                <col>
                            </colgroup>
                            <tbody>
                            <tr>
                                <td><span class="tit">版本信息:</span></td>
                                <td><span class="info iframe"> V01_UTF8_0.01 ( release版 )</span></td>
                            </tr>
                            <tr>
                                <td><span class="tit">所属公司:</span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><span class="tit">官网地址:</span></td>
                                <td><span class="info"><a href="http://www.biaozhuanjia.com/" class="official"
                                                          target="_blank">http://www.biaozhuanjia.com</a></span></td>
                            </tr>
                            <tr>
                                <td><span class="tit">社区交流:</span></td>
                                <td><a href="javascript:;" class="layui-btn layui-btn-small layui-btn-danger">微信群</a>
                                    <a href="javascript:;" class="layui-btn layui-btn-small">公众号</a><em class="qq-add">（客服QQ:1656446238）</em>
                                </td>
                            </tr>
                            <tr>
                                <td><span class="tit">客服电话:</span></td>
                                <td>18611410103</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
            <!-- 日志更新 -->
            <section class="panel">
                <header class="panel-heading bm0">
                    <span class="span-title">更新日志</span>
                    <span class="tools pull-right"><a href="javascript:;" class="iconpx-chevron-down"></a></span>
                </header>
                <div class="panel-body">
                    <div class="update-log">
                        <h2>V01_UTF8_1.09 ( Release版 ) 2017-07-30</h2>
                        <ul>
                            <li>#<p>tab选项卡针对不能切换关闭的问题修复</p></li>
                            <li>#<p>增加json生成三级菜单tab选项卡切换</p></li>
                            <li>#<p>tab选项卡内页增加添加按钮，增加页面在选项卡面板上打开</p></li>
                            <li>#<p>tab选项卡增加常用操作控制功能（如关闭系列、刷新）</p></li>
                            <li>#<p>tab选项卡溢出左右滑动和自动定位当前选项卡</p></li>
                            <li>#<p>主题设置功能，可选主题（目前提供默认、深蓝、墨绿主题 后期提供自定义配色主题设置）</p></li>
                            <li>#<p>全屏切换配合主题设置，模拟F11全屏</p></li>
                            <li>#<p>tab选项卡内按钮在选项卡面板打开定位</p></li>
                            <li><p>注：也许当前你看的17ppd模板只有一个基础的空架子并没有多少实用功能，如果你喜欢它，别灰心在后续更新中···它会变强大的</p></li>
                            <li>
                                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                                    <legend>未完待续</legend>
                                </fieldset>

                                <fieldset class="layui-elem-field content">
                                    <legend>下个版本更新</legend>
                                    <div class="layui-field-box">
                                        <p>
                                            如：datatable、json数据表格分页、基于flex的grid布局、通用菜单按钮级权限模块等模块功能这些会在后续更新中相继推出（当然有一些后台最常用的功能在官方2.0中可能会出现，同时十分期待2.0，有时候不需要重复造轮子，在后期更新中选择性更新，目前只是一个cms后台通用模版，有瑕疵和改进的地方希望大家帮忙多提建议！待17ppd整体完成后分享基于Think+聊天功能的cms系统）</p>
                                    </div>
                                </fieldset>

                            </li>
                        </ul>
                    </div>
                </div>
            </section>
        </div>

        <div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
            <section class="panel">
                <header class="panel-heading bm0">
                    <span class="span-title">系统公告</span>
                    <span class="tools pull-right"><a href="javascript:;" class="iconpx-chevron-down"></a></span>
                </header>
                <div class="panel-body">
                    <table class="table table-hover personal-task">
                        <tbody>
                        <tr>
                            <td class="top-top">
                                <p class="larry_github">
                                    在这里非常感谢各位拍客对标专家的信任；
                                    <br>谢谢！
                                    <br>
                                    分享地址：<a href="https://www.biaozhuanjia.com" class="githublink" target="_blank">https://www.biaozhaunjia.com</a>
                                    <br>
                                    标专家用户交流群：<span>144967008</span>
                                    <a href="javascript:;">
                                        <img border="0" src="/res/images/qqgroup.png" title="官方交流群"></a>
                                    <br>
                                    微信客服手机号:<span>13622034018</span>
                                </p>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="panel">
                <header class="panel-heading bm0">
                    <span class="span-title">数据报表</span>
                    <span class="tools pull-right"><a href="javascript:;" class="iconpx-chevron-down"></a></span>
                </header>
                <div class="panel-body larry-seo-box">
                    <div class="larry-seo-stats" id="larry-seo-stats"
                         style="-webkit-tap-highlight-color: transparent; user-select: none; position: relative; background: transparent;"
                         _echarts_instance_="ec_1503104841159">
                        <div style="position: relative; overflow: hidden; width: 460px; height: 270px; padding: 0px; margin: 0px; border-width: 0px;">
                            <canvas width="920" height="540" data-zr-dom-id="zr_0"
                                    style="position: absolute; left: 0px; top: 0px; width: 460px; height: 270px; user-select: none; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); padding: 0px; margin: 0px; border-width: 0px;"></canvas>
                        </div>
                        <div></div>
                    </div>
                </div>
            </section>

            <section class="panel">
                <header class="panel-heading bm0">
                    <span class="span-title">最新策略</span>
                    <span class="tools pull-right"><a href="javascript:;" class="iconpx-chevron-down"></a></span>
                </header>
                <div class="panel-body">
                    <form class="layui-form larry-document" action="http://demo.17ppd.com/backstage/html/main.php"
                          method="post">
                        <table class="layui-table">
                            <colgroup>
                                <col>
                                <col width="100">
                                <col width="120">
                                <col width="100">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>策略名称</th>
                                <th>使用量</th>
                                <th>发布时间</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><a href="javascript:;">博士+研究生+A-D+20%+2000-500+首次</a></td>
                                <td>20</td>
                                <td>2017-03-25</td>
                                <td class="layui-form-item">
                                    <input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">

                                    <div class="layui-unselect layui-form-switch" lay-skin="_switch"><em>OFF</em><i></i>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td><a href="javascript:;">博士+研究生+A-D</a></td>
                                <td>2000</td>
                                <td>2017-03-25</td>
                                <td class="layui-form-item">
                                    <input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">

                                    <div class="layui-unselect layui-form-switch" lay-skin="_switch"><em>OFF</em><i></i>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td><a href="javascript:;">博士+研究生+A-D</a></td>
                                <td>80000</td>
                                <td>2017-03-25</td>
                                <td class="layui-form-item">
                                    <input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">

                                    <div class="layui-unselect layui-form-switch" lay-skin="_switch"><em>OFF</em><i></i>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </section>
        </div>

    </div>

</div>

<@common.includeCommonJs/>
<script src="/res/mods/main.js"></script>
<script type="text/javascript" src="/res/mods/echarts.min.js"></script>
</body>

</html>

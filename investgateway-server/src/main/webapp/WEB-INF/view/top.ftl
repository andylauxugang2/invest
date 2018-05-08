
<input id="userIdData" value="${userId}" hidden="true"/>
<input id="showWelcomeTipData" value="${showWelcomeTipData!}" hidden="true"/>
<div class="layui-header header">
    <div class="layui-main">
        <!-- logo区域 -->
        <div class="admin-logo-box">
            <a class="logo" href="javascript:;" title="logo"><span>拍拍绑首页</span></a>
            <!-- 缩放屏幕按钮 -->
            <div class="andy-side-menu">
                <i class="fa fa-bars" aria-hidden="true"></i>
            </div>
        </div>
        <!-- 搜索 -->
        <div class="admin-search-box">
            <!--<input type="text" value="search" style="">-->
        </div>
        <!-- 中间内容 -->
        <div class="admin-center-box">

        </div>
        <!-- 右侧导航 -->
        <div class="andy-header-nav">
            <ul class="layui-nav andy-header-item">
                <li class="layui-nav-item layui-this"><a href="">首页</a></li>
                <li class="layui-nav-item">
                <#if messageCount?? && messageCount!="0">
                    <a id="zhuanjiaNoticeHref" href="javascript:;" data-url="/usernotifymessageview?userId=${user.id}" data-icon="icon-duanxin2"><span>专家消息</span><em class="layui-badge">${messageCount}</em></a>
                <#else>
                    <a id="zhuanjiaNoticeHref" href="javascript:;" data-url="/usernotifymessageview?userId=${user.id}" data-icon="icon-duanxin2"><span>专家消息</span></a>
                </#if>
                </li>
                <li class="layui-nav-item"><a href="">产品介绍</a></li>
                <li class="layui-nav-item"><a href="">专家帮助</a></li>
                <li class="layui-nav-item">
                    <a href="javascript:;" class="avatar">
                    <#if user.headImg?? && user.headImg!="">
                        <img src="data:image/png;base64,${user.headImg}" id="topUserFace">
                    <#else>
                        <img src="/res/images/face.jpg" id="topUserFace">
                    </#if>
                        <cite>${user.nick}</cite>
                        <i>${user.mobile}</i>
                    </a>
                    <dl class="layui-nav-child layui-anim layui-anim-upbit"> <!-- 二级菜单 -->
                        <dd><a href="javascript:;" data-url="/setuserinfoview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-geren" data-icon="icon-geren"></i><span>个人信息</span></a></dd>
                        <dd><a href="javascript:;" data-url="/resetpwdview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-mima"></i><span>修改密码</span></a></dd>
                        <dd><a href="javascript:;" data-url="/accountinfoview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-zhifu"></i><span>我的账户</span></a></dd>
                        <dd><a href="javascript:;" data-url="/setuserinfoview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-system"></i><span>我的账单</span></a></dd>
                        <dd><a href="javascript:;" data-url="/setuserinfoview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-shezhi"></i><span>系统设置</span></a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item"><a href="javascript:;" class="sub-nav-item" id="logout-btn"><i class="iconfont icon-power"></i><span>退出</span></a></li>
            </ul>
        </div>
    </div>
</div>
<@common.includeCommonJs/>
<script src="/res/mods/top.js"></script>
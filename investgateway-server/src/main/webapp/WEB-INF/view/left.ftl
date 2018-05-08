<div class="layui-side layui-bg-black" id="andy-side">
    <div class="layui-side-scroll" id="andy-nav-side" lay-filter="side">
        <div class="user-photo">
            <p>亲爱的拍客，欢迎使用标专家！</p>
        </div>
        <ul class="layui-nav layui-nav-tree">
            <!-- 侧边导航: <ul class="layui-nav layui-nav-tree layui-nav-side"> -->
            <#--<li class="layui-nav-item layui-nav-itemed">-->
        <#if showWelcomeTipData?? && showWelcomeTipData=="true">
            <li class="layui-nav-item layui-nav-itemed">
        <#else>
            <li class="layui-nav-item">
        </#if>
                <a href="javascript:;" class="sub-nav-item"><i class="iconfont icon-jiaoseguanli4"></i>标专家策略管理</a>
                <dl class="layui-nav-child andy-nav-child">
                    <dd><a id="gotoMainPolicyHref" href="javascript:;" data-url="/ivgatepolicy/mainpolicyview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>主策略</span></a></dd>
                    <dd><a id="gotoSysLoanPolicyHref" href="javascript:;" data-url="/ivgatepolicy/usersysloanpolicyview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-chajian" data-icon='icon-chajian'></i><span>系统推荐散标</span></a></dd>
                    <dd><a href="javascript:;" data-url="/ivgatepolicy/userloanpolicyview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-chajian" data-icon='icon-chajian'></i><span>散标自定义</span></a></dd>
                    <dd><a href="javascript:;" data-url="javascript:;" class="sub-nav-item"><i class="iconfont icon-role" data-icon='icon-role'></i><span>跟投</span></a></dd>
                    <dd><a href="javascript:;" data-url="javascript:;" class="sub-nav-item"><i class="iconfont icon-a130" data-icon='icon-a130'></i><span>债权策略</span></a></dd>
                    <dd><a href="javascript:;" data-url="javascript:;" class="sub-nav-item"><i class="iconfont icon-rizhi" data-icon='icon-rizhi'></i><span>操作记录</span></a></dd>
                </dl>
            </li>
        <#if showWelcomeTipData?? && showWelcomeTipData=="true">
            <li class="layui-nav-item layui-nav-itemed">
        <#else>
            <li class="layui-nav-item">
        </#if>
                <a id="viewLoanRecordHref" href="javascript:;" class="sub-nav-item"><i class="iconfont icon-zhifu"></i>投资管理</a>
                <dl class="layui-nav-child andy-nav-child">
                    <dd><a href="javascript:;" data-url="/record/investrecordview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-chaxun1" data-icon='icon-chaxun1'></i><span>投资记录查询</span></a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="javascript:;" class="sub-nav-item"><i class="iconfont icon-icon1"></i>投资分析</a>
                <dl class="layui-nav-child andy-nav-child">
                    <dd><a href="javascript:;" data-url="/overduedataview" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>逾期数据分析</span></a></dd>
                    <dd><a href="javascript:;" data-url="javascript:;" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>收益率分析</span></a></dd>
                    <dd><a href="javascript:;" data-url="javascript:;" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>投资成功率</span></a></dd>
                </dl>
            </li>
        <#if showWelcomeTipData?? && showWelcomeTipData=="true">
            <li class="layui-nav-item layui-nav-itemed">
        <#else>
            <li class="layui-nav-item">
        </#if>
                <a href="javascript:;" class="sub-nav-item"><i class="iconfont icon-icon1"></i>第三方信息管理</a>
                <dl class="layui-nav-child andy-nav-child">
                    <dd><a id="gotoUserAuthHref" href="javascript:;" data-url="/third/authview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>拍拍贷用户授权</span></a></dd>
                    <dd><a href="javascript:;" data-url="/third/userinfoview?userId=${user.id}" class="sub-nav-item"><i class="iconfont icon-icon1" data-icon='icon-icon1'></i><span>拍拍贷账户信息</span></a></dd>
                </dl>
            </li>
        </ul>
    </div>
</div>
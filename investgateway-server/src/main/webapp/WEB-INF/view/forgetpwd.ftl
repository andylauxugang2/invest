<#import "./common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/forgetpwd.css">
</head>
<body>
<div class="layui-layout">
    <div class="layui-header header">
        <div class="layui-main">
            <!-- logo区域 -->
            <div class="admin-logo-box">
                <a class="logo" href="" title="logo"><span>拍拍绑首页</span></a>
            </div>
            <!-- 中间内容 -->
            <div class="admin-center-box">

            </div>
            <!-- 右侧导航 -->
            <ul class="layui-nav andy-header-item">
                <li class="layui-nav-item layui-this"><a href="">产品介绍</a></li>
                <li class="layui-nav-item"><a href="">专家帮助</a></li>
            </ul>
        </div>
    </div>

    <div class="main layui-clear">

        <div class="andy-panel andy-panel-user" style="padding: 20px;">
            <div class="layui-tab layui-tab-brief">
                <ul class="layui-tab-title">
                    <li><a href="/register">新用户注册</a></li>
                    <li class="layui-this"><a href="/forgetPassword">找回密码</a></li>
                </ul>
                <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
                    <div class="layui-tab-item layui-show">
                        <div class="layui-form layui-form-pane">
                            <form method="post">
                                <div class="layui-form-item">
                                    <label for="L_phone" class="layui-form-label">手机号</label>

                                    <div class="layui-input-inline">
                                        <input type="text" id="L_phone" name="phone" required autocomplete="off"
                                               class="layui-input" placeholder="请填写11手机号" lay-verify="phone">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">注册时使用到的手机号码</div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_check_code" class="layui-form-label">图形验证码</label>
                                    <div class="layui-input-inline">
                                        <input type="text" id="L_image_check_code" name="imageCheckCode" required
                                               autocomplete="off" class="layui-input" placeholder="请填写图形验证码"
                                               lay-verify="imageCheckCode">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">
                                        <img src="data:image/png;base64," class="layui-circle" id="imageCheckCode">
                                        <a id="imageCheckCodeLink" href="javascript:;"><em
                                            class="iconfont icon-shuaxin1" style="margin-left: 5px;"></em></a></div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_check_code" class="layui-form-label">短信验证码</label>

                                    <div class="layui-input-inline">
                                        <input type="text" id="L_check_code" name="checkCode" required
                                               autocomplete="off" class="layui-input" placeholder="请填写6位纯数据验证码"
                                               lay-verify="checkCode">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux"><a id="checkCodeLink" href="javascript:;">获取短信验证码</a><em
                                            class="iconfont icon-duanxin" style="margin-left: 5px;"></em></div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_pass" class="layui-form-label">密码</label>

                                    <div class="layui-input-inline">
                                        <input type="password" id="L_pass" name="pass" required
                                               autocomplete="off" class="layui-input" lay-verify="pass">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">登录密码必须是6-20位字母与数字的组合</div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_repass" class="layui-form-label">确认密码</label>

                                    <div class="layui-input-inline">
                                        <input type="password" id="L_repass" name="repass" required
                                               lay-verify="repass" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <button class="layui-btn" lay-filter="resetPwdfilter" lay-submit>重置密码</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>

<div class="footer">
    <p><a href="http://www.17ppd.com/">捉宝网-标专家</a> 2017 &copy; <a href="http://www.17ppd.com/">17ppd.com</a></p>

    <p>
        <a href="http://www.17ppd.com/auth/get" target="_blank">产品授权</a>
        <a href="http://www.17ppd.com/jie/8157.html" target="_blank">获取捉宝网</a>
        <a href="http://www.17ppd.com/jie/2461.html" target="_blank">微信公众号</a>
    </p>
</div>

<@common.includeCommonJs/>
<script src="/res/mods/forgetpwd.js"></script>
</body>
</html>


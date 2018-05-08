'use strict';
layui.use(['jquery', 'layer', 'element'], function () {
    window.jQuery = window.$ = layui.jquery;
    window.layer = layui.layer;
    var element = layui.element;
});

//邮箱格式验证
layui.use(['form', 'layedit', 'laydate'], function () {
    var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;

    var password;
    //自定义验证规则
    form.verify({
        checkCode: function (value) {
            if (value.length == 0) {
                return '请输入短信验证码';
            }
        },
        imageCheckCode: function (value) {
            if (value.length == 0) {
                return '请输入图片验证码';
            }
        },
        pass: function (value) {
            if (value.length < 6) {
                return '请输入至少6位的密码';
            }
            if (value.length > 20) {
                return '请输入少于20位的密码';
            }
            password = value;
        },
        repass: function (value) {
            if (value.length == 0) {
                return '请输入确认密码';
            }
            if (password != value) {
                return '两次密码输入有误,请确保密码输入一致';
            }
        }
        //, phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']
        //, email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
    });

    //创建一个编辑器
    //layedit.build('LAY_demo_editor');

    //监听提交
    form.on('submit(resetPwdfilter)', function (data) {
        /*layer.alert(JSON.stringify(data.field), {
         title: '最终的提交信息'
         })*/
        var param = jQuery.extend({}, data.field);
        var paramStr = JSON.stringify(param);
        var model = layer.load(2);
        $.ajax({
            type: "post",
            url: "/user/forgetPwd",
            data: paramStr,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    var index = layer.msg('修改成功，正在跳转登录页面，请稍候', {icon: 16, time: false, shade: 0.8});
                    setTimeout(function () {
                        layer.close(index);
                        location.href = "/login";
                    }, 1000);
                } else {
                    layer.msg(data.msg);
                }
                layer.close(model);
            },
            error: function (e) {
                layer.msg("未知异常!");
                layer.close(model);
            }
        });
        return false;
    });

    //获取短信验证码
    var validCode = true;
    $('#checkCodeLink').click(function () {
        var time = 60;
        var code = $(this);
        if (validCode) {
            var mobile = $('#L_phone').val();
            if (!(/^1[3|4|5|7|8]\d{9}$/.test(mobile))) {
                layer.msg("请输入正确的手机号");
                return false;
            }

            var imageCode = $('#L_image_check_code').val();
            var param = {"phone": mobile, "imageCheckCode": imageCode};
            //发送验证码
            var paramStr = JSON.stringify(param);
            $.ajax({
                type: "post",
                url: "/user/getForgetPwdSMSCode",
                data: paramStr,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == 0) {
                        layer.msg("短信验证码发送成功,请查收手机短信");
                        code.html("重新获取:" + time + "秒");
                        validCode = false;
                        code.addClass("check-code");
                        var t = setInterval(function () {
                            time--;
                            code.html("重新获取:" + time + "秒");
                            if (time == 0) {
                                clearInterval(t);
                                code.html("重新获取");
                                validCode = true;
                                //code.removeClass("check-code");
                            }
                        }, 1000)
                    } else {
                        layer.msg(data.msg);
                        getImageCheckCode();
                        //$("#img5").src="getcheckcode?t=" + Math.random();
                        //$("[name=checkCode]").val("");
                    }
                },
                error: function (e) {
                    layer.msg("未知异常!");
                }
            });

        }
    });

    function getImageCheckCode() {
        $.ajax({
            type: "get",
            url: "/user/getCheckCode",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    $('#imageCheckCode').attr('src', 'data:image/png;base64,' + data.data);
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (e) {
                layer.msg("未知异常!");
            }
        });
    }

    getImageCheckCode();
    //获取图形验证码
    $('#imageCheckCodeLink').click(function () {
        //发送验证码
        getImageCheckCode();
    });
});
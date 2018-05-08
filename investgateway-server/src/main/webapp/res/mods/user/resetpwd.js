'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'upload', 'laydate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        complete:function(XMLHttpRequest,textStatus){
            var httpStatus = XMLHttpRequest.status;
            if(httpStatus == xmlhttprequest_status_session_timeout){
                parent.location.href = "/login";
            }
            //alert(XMLHttpRequest.readyState);
        }
    });

    var userId = $('#userIdData').val();

    var password;
    //自定义验证规则
    form.verify({
        password: function (value) {
            if (value.length < 6) {
                return '请输入至少6位的密码';
            }
            if (value.length > 20) {
                return '请输入少于20位的密码';
            }
        },
        newPassword: function (value) {
            if (value.length < 6) {
                return '请输入至少6位的密码';
            }
            if (value.length > 20) {
                return '请输入少于20位的密码';
            }
            password = value;
        },

        newPassword2: function (value) {
            if (value.length == 0) {
                return '请输入确认密码';
            }
            if (password != value) {
                return '新密码两次输入有误,请确保密码输入一致';
            }
        }
    });

    //监听提交
    form.on('submit(resetPwd)', function (data) {
        var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
        var paramStr = JSON.stringify(data.field); //把一个对象转成一个序列化的JSON字符串
        $.ajax({
            type: "post",
            url: "/user/resetPwd",
            data: paramStr,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("修改密码成功");
                    }, 500);
                } else {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg(data.msg);
                    }, 500);
                }
            },
            error: function (e) {
                layer.msg("未知异常!");
            }
        });
        return false;
    });
})

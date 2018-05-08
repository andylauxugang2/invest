'use strict';
layui.use(['jquery', 'layer', 'element'], function () {
    window.jQuery = window.$ = layui.jquery;
    window.layer = layui.layer;
    var element = layui.element;
});

layui.config({
    base: "res/mods/"
}).use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //video背景
    $(window).resize(function () {
        if ($(".video-player").width() > $(window).width()) {
            $(".video-player").css({
                "height": $(window).height(),
                "width": "auto",
                "left": -($(".video-player").width() - $(window).width()) / 2
            });
        } else {
            $(".video-player").css({
                "width": $(window).width(),
                "height": "auto",
                "left": -($(".video-player").width() - $(window).width()) / 2
            });
        }
    }).resize();

    //登录按钮事件
    form.on('submit(loginfilter)', function (data) {
        var param = jQuery.extend({}, data.field);
        var paramStr = JSON.stringify(param);
        $.ajax({
            type: "post",
            url: "/user/login",
            data: paramStr,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    //登录成功去授权
                    location.href = "/userauth/gologin?userId=" + data.data.userId;
                } else {
                    layer.msg(data.msg, {
                        time: 1000 //2秒关闭（如果不配置，默认是3秒）
                    }, function () {
                        //do something
                    });
                    //$("#img5").src="getcheckcode?t=" + Math.random();
                    //$("[name=checkCode]").val("");
                }
            },
            error: function (e) {
                layer.msg("未知异常!");
            }
        });
        return false;
    });
})
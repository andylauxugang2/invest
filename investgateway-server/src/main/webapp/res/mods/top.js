'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'upload', 'laydate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;

    var userId = $('#userIdData').val();

    $("#logout-btn").click(function () {
        var _this = $(this);

        layer.confirm('确定退出系统？', {icon: 3, title: '提示信息'}, function (index) {
            var index = layer.msg('登出中，请稍候', {icon: 16, time: false, shade: 0.8});
            $.ajax({
                type: "get",
                url: "/user/logout?userId=" + userId,
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data.code == 0) {
                        setTimeout(function () {
                            layer.close(index);
                            location.href = "/login";
                        }, 500);
                    } else {
                        setTimeout(function () {
                            layer.close(index);
                            layer.msg(data.msg);
                        }, 500);
                    }
                },
                error: function (e) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("未知异常!");
                    }, 1000);
                }
            });
        })
    });
})
